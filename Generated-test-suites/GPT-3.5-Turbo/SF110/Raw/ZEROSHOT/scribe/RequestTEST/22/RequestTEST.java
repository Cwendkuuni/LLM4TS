import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnection(null);
        request.send();
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertNotNull(request.getHeaders().get("Content-Type"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        assertNotNull(request.getBodyParams().get("param1"));
        assertEquals("value1", request.getBodyParams().get("param1"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        assertNotNull(request.getQueryStringParams().get("param1"));
        assertEquals("value1", request.getQueryStringParams().get("param1"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload");
        assertNotNull(request.getBodyContents());
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload".getBytes());
        assertNotNull(request.getByteBodyContents());
        assertArrayEquals("payload".getBytes(), request.getByteBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        assertNotNull(request.getQueryStringParams().get("param1"));
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertNotNull(request.getQueryStringParams().get("param2"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertNotNull(request.getBodyParams().get("param1"));
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertNotNull(request.getBodyParams().get("param2"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNotNull(request.getUrl());
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?param1=value1");
        assertNotNull(request.getSanitizedUrl());
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload");
        assertNotNull(request.getBodyContents());
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload".getBytes());
        assertNotNull(request.getByteBodyContents());
        assertArrayEquals("payload".getBytes(), request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNotNull(request.getVerb());
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertNotNull(request.getHeaders().get("Content-Type"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNotNull(request.getCharset());
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertNotNull(request.getCharset());
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertNotNull(request.getConnection());
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNotNull(request.toString());
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}