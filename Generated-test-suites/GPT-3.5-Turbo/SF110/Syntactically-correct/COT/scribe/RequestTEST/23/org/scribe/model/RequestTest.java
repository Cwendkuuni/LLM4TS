
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testDoSend() throws IOException {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.createConnection();
        Response response = request.doSend();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() throws IOException {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        request.addHeaders(connection);
        assertNotNull(connection.getRequestProperty("Content-Length"));
    }
    
    @Test
    public void testAddBody() throws IOException {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        request.addBody(connection, "test".getBytes());
        assertNotNull(connection.getOutputStream());
    }
    
    @Test
    public void testAddHeader() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addHeader("Key", "Value");
        Map<String, String> headers = request.getHeaders();
        assertEquals("Value", headers.get("Key"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addBodyParameter("Key", "Value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("Value", bodyParams.get("Key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addQuerystringParameter("Key", "Value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("Value", queryStringParams.get("Key"));
    }
    
    @Test
    public void testAddPayloadString() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addPayload("payload".getBytes());
        assertArrayEquals("payload".getBytes(), request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Verb verb = Verb.GET;
        String url = "https://example.com?param=value";
        Request request = new Request(verb, url);
        request.addQuerystringParameter("Key", "Value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("Value", queryStringParams.get("Key"));
        assertEquals("value", queryStringParams.get("param"));
    }
    
    @Test
    public void testGetBodyParams() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addBodyParameter("Key", "Value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("Value", bodyParams.get("Key"));
    }
    
    @Test
    public void testGetUrl() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        assertEquals("https://example.com", request.getUrl());
    }
   
    @Test
    public void testGetSanitizedUrl() {
        Verb verb = Verb.GET;
        String url = "https://example.com:8080?param=value";
        Request request = new Request(verb, url);
        assertEquals("https://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addPayload("payload".getBytes());
        assertArrayEquals("payload".getBytes(), request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.addHeader("Key", "Value");
        Map<String, String> headers = request.getHeaders();
        assertEquals("Value", headers.get("Key"));
    }
    
    @Test
    public void testGetCharset() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        
        assertNotNull(request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.setReadTimeout(10, TimeUnit.SECONDS);
        
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        request.setConnectionKeepAlive(true);
        
        assertEquals(true, request.isConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        
        HttpURLConnection connection = new HttpURLConnection() {
            @Override
            public void disconnect() { }

            @Override
            public boolean usingProxy() { return false; }

            @Override
            public void connect() throws IOException { }
        };
        
        request.setConnection(connection);
        
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "https://example.com";
        Request request = new Request(verb, url);
        
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
}

