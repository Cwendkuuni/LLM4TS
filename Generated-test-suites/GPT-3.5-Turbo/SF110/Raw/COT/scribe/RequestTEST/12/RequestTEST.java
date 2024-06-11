import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Key", "Value");
        assertEquals("Value", request.getHeaders().get("Key"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("Key", "Value");
        assertEquals("Value", request.getBodyParams().get("Key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("Key", "Value");
        assertEquals("Value", request.getQueryStringParams().get("Key"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("Payload");
        assertEquals("Payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadWithBytes() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, request.getByteBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?param=value");
        assertEquals("http://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("Payload");
        assertEquals("Payload", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload(new byte[]{1, 2, 3});
        assertArrayEquals(new byte[]{1, 2, 3}, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Key", "Value");
        assertEquals("Value", request.getHeaders().get("Key"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
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
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnection().getKeepAlive());
    }

}