
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void testConstructors() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getQueryStringParams());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getHeaders());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
        
        request = new Request(Verb.POST, "http://example.com");
        assertEquals(Verb.POST, request.getVerb());
        assertEquals("http://example.com", request.getUrl());
        assertNotNull(request.getQueryStringParams());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getHeaders());
        assertNull(request.getPayload());
        assertNull(request.getBytePayload());
        assertFalse(request.isConnectionKeepAlive());
    }
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        assertTrue(request.getHeaders().containsKey("Content-Type"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "testuser");
        assertTrue(request.getBodyParams().containsKey("username"));
        assertEquals("testuser", request.getBodyParams().get("username"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("page", "1");
        assertTrue(request.getQueryStringParams().containsKey("page"));
        assertEquals("1", request.getQueryStringParams().get("page"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload("test payload");
        assertEquals("test payload", request.getPayload());
    }
    
    @Test
    public void testAddBytePayload() {
        Request request = new Request(Verb.GET, "http://example.com");
        byte[] payload = { 0x01, 0x02, 0x03 };
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?param1=value1&param2=value2");
        assertEquals("http://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("param", "value");
        assertEquals("param=value", request.getBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNull(request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        // No assertion, just testing for any exceptions
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        // No assertion, just testing for any exceptions
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
        request.setConnectionKeepAlive(false);
        assertFalse(request.isConnectionKeepAlive());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}
