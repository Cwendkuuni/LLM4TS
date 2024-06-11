import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Test send method
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        // Test addHeader method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        // Test addBodyParameter method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("value1", request.getBodyParams().get("param1"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        // Test addQuerystringParameter method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        assertEquals("value1", request.getQueryStringParams().get("param1"));
    }
    
    @Test
    public void testAddPayload_String() {
        // Test addPayload method with string parameter
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getByteBodyContents());
    }
    
    @Test
    public void testAddPayload_ByteArray() {
        // Test addPayload method with byte array parameter
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        // Test getQueryStringParams method
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        // Test getBodyParams method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("value1", request.getBodyParams().get("param1"));
    }
    
    @Test
    public void testGetUrl() {
        // Test getUrl method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        // Test getSanitizedUrl method
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        // Test getBodyContents method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("param1=value1", request.getBodyContents());
    }
    
    @Test
    public void testgetByteBodyContents() {
        // Test getByteBodyContents method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        byte[] expected = "param1=value1".getBytes();
        assertArrayEquals(expected, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        // Test getVerb method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        // Test getHeaders method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        // Test getCharset method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        // Test setConnectTimeout method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        // Check if connection timeout is set correctly
    }
    
    @Test
    public void testSetReadTimeout() {
        // Test setReadTimeout method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        // Check if read timeout is set correctly
    }
    
    @Test
    public void testSetCharset() {
        // Test setCharset method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        // Test setConnectionKeepAlive method
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        // Test setConnection method
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        // Test toString method
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }

}