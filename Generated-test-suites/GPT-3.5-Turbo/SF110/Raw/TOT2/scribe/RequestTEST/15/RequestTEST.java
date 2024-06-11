import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {
    
    private Request request;
    
    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://example.com");
    }
    
    @Test
    public void testSend() {
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param", "value");
        assertEquals("value", request.getQueryStringParams().get("param"));
    }
    
    @Test
    public void testAddPayload_String() {
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());
    }
    
    @Test
    public void testAddPayload_Byte() {
        request.addPayload(new byte[]{1, 2, 3});
        assertEquals(3, request.getBytePayload().length);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testSetConnectTimeout() {
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
    
}