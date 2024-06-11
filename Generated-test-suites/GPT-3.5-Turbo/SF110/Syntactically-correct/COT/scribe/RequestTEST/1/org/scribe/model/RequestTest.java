
import org.junit.*;
import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://example.com");
        Response response = request.send();
        
        assertNotNull(response);
        // Additional assertions for the response can be added
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addHeader("Content-Type", "application/json");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("name", "John");
        request.addBodyParameter("age", "25");
        
        assertEquals("John", request.getBodyParams().get("name"));
        assertEquals("25", request.getBodyParams().get("age"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addQuerystringParameter("page", "1");
        request.addQuerystringParameter("limit", "10");
        
        assertEquals("1", request.getQueryStringParams().get("page"));
        assertEquals("10", request.getQueryStringParams().get("limit"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addPayload("payload data");
        
        assertEquals("payload data", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.POST, "https://example.com");
        byte[] payload = "payload data".getBytes();
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://example.com");
        
        assertEquals("https://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://example.com?param=value");
        
        assertEquals("https://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("name", "John");
        
        assertEquals("name=John", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("name", "John");
        
        byte[] expected = "name=John".getBytes();
        assertArrayEquals(expected, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://example.com");
        
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        // Test the timeout value or behavior, e.g. assert actual connection timeout
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        
        // Test the timeout value or behavior, e.g. assert actual read timeout
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.setCharset("UTF-16");
        
        assertEquals("UTF-16", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.getHeaders().containsKey("Connection"));
        assertEquals("keep-alive", request.getHeaders().get("Connection"));
    }
    
    // Additional test cases for other methods can be added
    
}

