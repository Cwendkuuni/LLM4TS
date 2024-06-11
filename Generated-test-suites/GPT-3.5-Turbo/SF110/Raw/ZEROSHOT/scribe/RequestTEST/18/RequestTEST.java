import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestTest {
    
    @Test
    public void testSend() {
        try {
            Request request = new Request(Verb.GET, "http://www.example.com");
            Response response = request.send();
            
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateConnection() {
        try {
            Request request = new Request(Verb.GET, "http://www.example.com");
            request.createConnection();
            
            assertNotNull(request.getConnection());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSend() {
        try {
            Request request = new Request(Verb.GET, "http://www.example.com");
            HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.setConnection(connection);
            
            Response response = request.doSend();
            
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeaders() {
        try {
            Request request = new Request(Verb.GET, "http://www.example.com");
            HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.setConnection(connection);
            
            request.addHeader("Content-Type", "application/json");
            
            assertEquals("application/json", connection.getRequestProperty("Content-Type"));
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddBody() {
        try {
            Request request = new Request(Verb.POST, "http://www.example.com");
            HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.setConnection(connection);
            
            byte[] content = "Test Payload".getBytes();
            
            request.addBody(connection, content);
            
            assertNotNull(connection.getOutputStream());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("key", "value");
        
        assertEquals("value", request.getQueryStringParams().get("key"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("Test Payload");
        
        assertEquals("Test Payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        request.addQuerystringParameter("key2", "value2");
        
        assertEquals("value", request.getQueryStringParams().get("key"));
        assertEquals("value2", request.getQueryStringParams().get("key2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("Test Payload");
        
        assertEquals("Test Payload", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        assertEquals(5000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        
        assertEquals(true, request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = (HttpURLConnection) new URL("http://www.example.com").openConnection();
        request.setConnection(connection);
        
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}