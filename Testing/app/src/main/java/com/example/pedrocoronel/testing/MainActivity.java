package com.example.pedrocoronel.testing;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


public class MainActivity extends ActionBarActivity {

    private Button btnBuscar ;
    private EditText txtCI;
    private TextView txtRespuesta ;
    private HttpClient httpClient;
    private HttpResponse httpResponse;
    private String url;
    private static final String URL_afiliaciones = "http://infopadron.cmelgarejo.net/api/afiliaciones/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        btnBuscar = (Button)findViewById(R.id.btn_buscar);
        txtRespuesta = (TextView)findViewById(R.id.txt_respuesta);


        txtCI = (EditText)findViewById(R.id.txt_ci);

        httpClient = new DefaultHttpClient();



        btnBuscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                url = URL_afiliaciones+txtCI.getText().toString();

                txtRespuesta.setText("");

                BufferedReader in = null;
                String data = null;

                try{
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpGet request = new HttpGet();
                    URI website = new URI(url);
                    request.setURI(website);
                    HttpResponse response = httpclient.execute(request);
                    in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));

                    // NEW CODE
                    String line = in.readLine();

                    JSONObject jObj = null;

                    try {
                        jObj = new JSONObject(line);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }

                    if(jObj.getInt("status") == -1)
                    {
                        txtRespuesta.append(jObj.getString("msg"));
                    }
                    else {

                        String nombre = jObj.getString("nombre_completo");
                        txtRespuesta.append("Nombre: "+ nombre);

                        txtRespuesta.append(System.getProperty("line.separator"));
                        txtRespuesta.append(System.getProperty("line.separator"));

                        JSONArray Afilliaciones = jObj.getJSONArray("afiliaciones");

                        for (int i = 0; i < Afilliaciones.length(); i++) {

                            JSONObject obj = Afilliaciones.getJSONObject(i);
                            String pars = obj.getString("partido");
                            txtRespuesta.append("Partido: "+ pars);
                            txtRespuesta.append(System.getProperty("line.separator"));
                            txtRespuesta.append(System.getProperty("line.separator"));


                            pars = obj.getString("lugar_votacion");
                            txtRespuesta.append("Lugar de Votacion: "+ pars);
                            txtRespuesta.append(System.getProperty("line.separator"));
                            txtRespuesta.append(System.getProperty("line.separator"));

                            pars = obj.getString("mesa");
                            txtRespuesta.append("Mesa: "+ pars);
                            txtRespuesta.append(System.getProperty("line.separator"));
                            txtRespuesta.append("____________________");
                            txtRespuesta.append(System.getProperty("line.separator"));


                        }
                    }


                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }




            }
        });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
