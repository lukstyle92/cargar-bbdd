/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JavaApplication1 {

    private int idReceta;
    private String nombre;
    private String tipo;
    private String dificultad;
    private int comensales;
    private String preparacion;
    private String ingredientes;
    private String path;
    private String video;

    public JavaApplication1() {
        this.idReceta = -1;
        this.nombre = "";
        this.tipo = "";
        this.dificultad = "";
        this.comensales = -1;
        this.preparacion = "";
        this.path = "../../assets/path/noimage.png";
        this.video = "";
    }

    public JavaApplication1(int idReceta, String nombre, String tipo, String dificultad, int comensales, String preparacion, String ingredientes, String video) {
        this.idReceta = idReceta;
        this.nombre = nombre;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.comensales = comensales;
        this.preparacion = preparacion;
        this.ingredientes = ingredientes;
        this.video = video;
    }

    public ArrayList<String> obtenerLinks(String url) {
        ArrayList<String> listaLinks = new ArrayList<>();
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Elements articulos = doc.getElementsByClass("itemreceta");
            for (Element articulo : articulos) {
                //El selector span:nth-child(x) busca al padre de span y elige al elemento hijo en la posición x
                listaLinks.add(articulo.child(0).getElementsByTag("a").attr("href"));
            }
        } catch (Exception ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
        return listaLinks;
    }

    public void obtenerNombre(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            this.nombre = doc.getElementsByTag("h1").text();
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerImagenes(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element el = doc.getElementById("recipe");
            Elements els = el.getElementsByTag("img");
            this.path = els.attr("src");
            System.out.println("");
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerTipo(String url) {
        this.tipo = "comida";
    }

    public void obtenerDificultad(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element articulo = doc.getElementById("extrainfo");
            //El selector span:nth-child(x) busca al padre de span y elige al elemento hijo en la posición x
            this.dificultad = articulo.child(1).getElementsByTag("li").text().split(" ")[0];
        } catch (Exception ex) {
            System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
        }
    }

    public void obtenerCantidadComensales(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element articulo = doc.getElementById("extrainfo");
            //El selector span:nth-child(x) busca al padre de span y elige al elemento hijo en la posición x
            this.comensales = Integer.parseInt(articulo.child(1).getElementsByTag("li").text().split(" ")[4]);
        } catch (NumberFormatException nfe) {
            System.out.println("Error al obtener el número de comensales.");
            nfe.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void obtenerModoPreparacion(String url) {
        Response response = null;
        try {
            String tituloPreparacion = "";
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element el = doc.getElementsByAttributeValue("itemprop", "recipeInstructions").get(0);
            Elements els = el.children();
            int contador = 1;
            for (int i = 0; i < els.size(); i++) {
                tituloPreparacion = els.get(i).getElementsByTag("h3").text();
                if (!tituloPreparacion.isEmpty()) {
                    this.preparacion += tituloPreparacion + ";";
                }
                if (els.get(i).getElementsByTag("li").size() > 0) {
                    for (int j = 0; j < els.get(i).getElementsByTag("li").size(); j++) {
                        this.preparacion += els.get(i).getElementsByTag("li").get(j).text() + ";";
                        contador++;
                    }
                    contador = 1;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerIngredientes(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element articulo = doc.getElementById("ingredients");
            Elements el = articulo.getElementsByTag("li");
            for (Element element : el) {
                this.ingredientes += element.text() + ";";
            }
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getCantidadComensales() {
        return comensales;
    }

    public void setCantidadComensales(int comensales) {
        this.comensales = comensales;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String sendJSONData(String message, String url) throws Exception {
        //creating map object to create JSON object from it
        Map<String, Object> jsonValues = new HashMap<String, Object>();
        JSONObject json1 = new JSONObject(message);
        jsonValues.put("nombre", json1.getString("nombre"));
        jsonValues.put("tipo", json1.getString("tipo"));
        jsonValues.put("dificultad", json1.getString("dificultad"));
        jsonValues.put("comensales", json1.get("comensales"));
        jsonValues.put("preparacion", json1.getString("preparacion"));
        jsonValues.put("ingredientes", json1.getString("ingredientes"));
        jsonValues.put("path", json1.getString("path"));
        JSONObject json = new JSONObject(jsonValues);
        StringEntity entity = new StringEntity(message,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        return null;
    }

    public static void main(String[] args) {
        //try {
        /*TestAplicationURL t = new TestAplicationURL(
         1280,
         "Que ensalada ni que ensalada",
         "cena",
         "Casi imposible",
         1,
         "Se come crudo como el sushi",
         "Agua y pan");
         Gson gson = new Gson();
         gson.toJson(t);
         String url = "http://localhost:8080/recetas/rest/servicios/edit";
         String respuesta = t.sendJSONData(gson.toJson(t), url);
         System.out.println(respuesta);*/
        JavaApplication1 t = new JavaApplication1();
        ArrayList<String> listaLinks = t.obtenerLinks("https://www.recetasderechupete.com/recetas-faciles/");
        for (int i = 0; i < listaLinks.size(); i++) {
            t.obtenerImagenes(listaLinks.get(i));
            t.obtenerNombre(listaLinks.get(i));
            t.obtenerTipo(listaLinks.get(i));
            t.obtenerDificultad(listaLinks.get(i));
            t.obtenerCantidadComensales(listaLinks.get(i));
            t.obtenerModoPreparacion(listaLinks.get(i));
            t.obtenerIngredientes(listaLinks.get(i));
            t.obtenerUrlVideo(listaLinks.get(i));
            String path = t.getImg();
            String nombre = t.getNombre();
            String tipo = t.getTipo();
            String dificultad = t.getDificultad();
            int comensales = t.getCantidadComensales();
            String preparacion = t.getPreparacion();
            String ingredientes = t.getIngredientes();
            String video = t.getUrlVideo();
            t.setImg(path);
            t.setNombre(nombre);
            t.setTipo(tipo);
            t.setDificultad(dificultad);
            t.setCantidadComensales(comensales);
            t.setPreparacion(preparacion);
            t.setIngredientes(ingredientes);
            t.setUrlVideo(video);
            Gson gson = new Gson();
            String url = "http://localhost:8080/rest/recetas";
            try {
                String respuesta = t.sendJSONData(gson.toJson(t), url);
            } catch (Exception ex) {
                Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
            }
            t.setNombre("");
            t.setTipo("");
            t.setDificultad("");
            t.setCantidadComensales(0);
            t.setPreparacion("");
            t.setIngredientes("");
        }
        /*} catch (Exception ex) {
         Logger.getLogger(TestAplicationURL.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    public String getImg() {
        return path;
    }

    public void setImg(String path) {
        this.path = path;
    }

    private void obtenerUrlVideo(String url) {
        Response response = null;
        try {
            response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(1000000).ignoreHttpErrors(true).execute();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
            Element el = doc.getElementById("recipe");
            Elements el1 = el.getElementsByTag("iframe");
            this.video = el1.attr("src");
        } catch (Exception ex) {
            Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getUrlVideo() {
        return this.video;
    }

    private void setUrlVideo(String video) {
        this.video = video;
    }

}
