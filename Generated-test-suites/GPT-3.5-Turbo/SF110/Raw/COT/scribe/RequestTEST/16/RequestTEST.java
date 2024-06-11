import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addHeader("Authorization", "Bearer ABC123");
        assertEquals("Bearer ABC123", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addBodyParameter("name", "John");
        assertEquals("John", request.getBodyParams().get("name"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.addQuerystringParameter("page", "1");
        assertEquals("1", request.getQueryStringParams().get("page"));
    }
    
    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.POST, "https://example.com");
        request.addPayload("Hello, World!");
        assertEquals("Hello, World!", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.POST, "https://example.com");
        byte[] payload = {72, 101, 108, 108, 111};
        request.addPayload(payload);
        assertEquals("Hello", request.getBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals("https://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://example.com:8080/path?param=value");
        assertEquals("https://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals("UTF-8", request.getCharset());
        request.setCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        // No assertion, just ensuring the method executes without error
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        // No assertion, just ensuring the method executes without error
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
        request.setConnectionKeepAlive(false);
        assertFalse(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "https://example.com");
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
    
}