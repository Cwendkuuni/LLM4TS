import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Response response = request.send();
        
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.createConnection();
        
        assertNotNull(request);
        assertNotNull(request.connection);
    }
    
    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Response response = request.doSend();
        
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        
        Map<String, String> headers = request.getHeaders();
        
        assertNotNull(headers);
        assertEquals(2, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        
        request.addBodyParameter("username", "testuser");
        request.addBodyParameter("password", "testpassword");
        
        byte[] content = request.getByteBodyContents();
        
        assertNotNull(content);
        assertEquals(27, content.length);
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        
        request.addBodyParameter("username", "testuser");
        request.addBodyParameter("password", "testpassword");
        
        Map<String, String> bodyParams = request.getBodyParams();
        
        assertNotNull(bodyParams);
        assertEquals(2, bodyParams.size());
        assertEquals("testuser", bodyParams.get("username"));
        assertEquals("testpassword", bodyParams.get("password"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        
        Map<String, String> queryStringParams = request.getQueryStringParams();
        
        assertNotNull(queryStringParams);
        assertEquals(2, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        
        request.addPayload("Test Payload");
        
        assertEquals("Test Payload", request.getSanitizedUrl());
        assertEquals("Test Payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        
        byte[] payload = "Test Payload".getBytes();
        
        request.addPayload(payload);
        
        byte[] bytePayload = request.getByteBodyContents();
        
        assertNotNull(bytePayload);
        assertEquals(payload.length, bytePayload.length);
        assertEquals("Test Payload", new String(bytePayload));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        String bodyContents = request.getBodyContents();
        
        assertNotNull(bodyContents);
        assertEquals("", bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        byte[] byteContents = request.getByteBodyContents();
        
        assertNotNull(byteContents);
        assertEquals(0, byteContents.length);
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Map<String, String> headers = request.getHeaders();
        
        assertNotNull(headers);
        assertTrue(headers.isEmpty());
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        String charset = request.getCharset();
        
        assertNotNull(charset);
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        int timeout = request.connection.getConnectTimeout();
        
        assertEquals(5000, timeout);
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        int timeout = request.connection.getReadTimeout();
        
        assertEquals(5000, timeout);
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.setCharset("UTF-16");
        
        String charset = request.getCharset();
        
        assertEquals("UTF-16", charset);
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.connectionKeepAlive);
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        HttpURLConnection connection = new HttpURLConnection(null);
        request.setConnection(connection);
        
        assertEquals(connection, request.connection);
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        String toString = request.toString();
        
        assertNotNull(toString);
        assertEquals("@Request(GET http://www.example.com)", toString);
    }
}