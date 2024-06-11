import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.net.HttpURLConnection;
import java.io.IOException;

public class RequestTest {
    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getQueryStringParams());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getHeaders());
    }
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.createConnection();
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        } catch(IOException e) {
            e.printStackTrace();
        }
        request.setConnection(connection);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer xxx");
        request.addHeader("Accept", "application/json");
        request.addHeader("User-Agent", "Mozilla/5.0");
        request.addHeaders(connection);
        
        assertEquals("application/json", connection.getRequestProperty("Content-Type"));
        assertEquals("Bearer xxx", connection.getRequestProperty("Authorization"));
        assertEquals("application/json", connection.getRequestProperty("Accept"));
        assertEquals("Mozilla/5.0", connection.getRequestProperty("User-Agent"));
    }
    
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://example.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        } catch(IOException e) {
            e.printStackTrace();
        }
        request.setConnection(connection);
        
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        request.addBody(connection, request.getByteBodyContents());
        
        assertEquals("param1=value1&param2=value2", new String(request.getByteBodyContents(), request.getCharset()));
        assertNotNull(connection.getOutputStream());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer xxx");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer xxx", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("payload");
        
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] payload = new byte[] {1, 2, 3};
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test(expected = MalformedURLException.class)
    public void testGetQueryStringParamsMalformedURLException() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.getUrl(); // calling getUrl to throw MalformedURLException
        request.getQueryStringParams();
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        
        assertEquals(2, request.getBodyParams().size());
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?key=value");
        
        assertEquals("http://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param1", "value1");
        String contents = request.getBodyContents();
        
        assertEquals("param1=value1", contents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param1", "value1");
        byte[] contents = request.getByteBodyContents();
        
        assertArrayEquals("param1=value1".getBytes(), contents);
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer xxx");
        
        assertEquals(2, request.getHeaders().size());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer xxx", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
        
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        
        assertEquals(1000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        
        assertEquals(1000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.isConnectionKeepAlive());
    }
}