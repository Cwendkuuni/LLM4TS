import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class RequestTest {
    
    @Test
    public void testSend_Success() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Response response = request.send();
        
        assertNotNull(response);
    }
    
    @Test(expected = OAuthException.class)
    public void testSend_Exception() {
        Request request = new Request(Verb.GET, "http://www.invalidurl.com");
        
        request.send();
    }
    
    @Test
    public void testCreateConnection_DefaultKeepAlive() throws Exception {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.createConnection();
        
        assertTrue(request.connection.getKeepAlive());
    }
    
    @Test
    public void testCreateConnection_NoKeepAlive() throws Exception {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(false);
        
        request.createConnection();
        
        assertFalse(request.connection.getKeepAlive());
    }
    
    @Test
    public void testDoSend_GET() throws Exception {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Response response = request.doSend();
        
        assertEquals(Verb.GET, request.connection.getRequestMethod());
    }
    
    @Test
    public void testDoSend_PUT() throws Exception {
        Request request = new Request(Verb.PUT, "http://www.example.com");
        
        Response response = request.doSend();
        
        assertEquals(Verb.PUT, request.connection.getRequestMethod());
    }
    
    @Test
    public void testDoSend_POST() throws Exception {
        Request request = new Request(Verb.POST, "http://www.example.com");
        
        Response response = request.doSend();
        
        assertEquals(Verb.POST, request.connection.getRequestMethod());
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addHeader("Content-Type", "application/json");
        
        assertEquals("application/json", request.headers.get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addBodyParameter("key", "value");
        
        assertEquals("value", request.bodyParams.get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addQuerystringParameter("key", "value");
        
        assertEquals("value", request.querystringParams.get("key"));
    }
    
    @Test
    public void testAddPayload_String() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.addPayload("payload");
        
        assertEquals("payload", request.payload);
    }
    
    @Test
    public void testAddPayload_Byte() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = {1, 2, 3};
        
        request.addPayload(payload);
        
        assertArrayEquals(payload, request.bytePayload);
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?key1=value1");
        request.addQuerystringParameter("key2", "value2");
        
        Map<String, String> queryStringParams = request.getQueryStringParams();
        
        assertEquals(2, queryStringParams.size());
        assertEquals("value1", queryStringParams.get("key1"));
        assertEquals("value2", queryStringParams.get("key2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key1", "value1");
        request.addBodyParameter("key2", "value2");
        
        Map<String, String> bodyParams = request.getBodyParams();
        
        assertEquals(2, bodyParams.size());
        assertEquals("value1", bodyParams.get("key1"));
        assertEquals("value2", bodyParams.get("key2"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        String url = request.getUrl();
        
        assertEquals("http://www.example.com", url);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        
        String sanitizedUrl = request.getSanitizedUrl();
        
        assertEquals("http://www.example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload");
        
        String bodyContents = request.getBodyContents();
        
        assertEquals("payload", bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents_PayloadExists() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        
        byte[] byteBodyContents = request.getByteBodyContents();
        
        assertArrayEquals(payload, byteBodyContents);
    }
    
    @Test
    public void testGetByteBodyContents_PayloadNull() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key", "value");
        byte[] expected = "key=value".getBytes();
        
        byte[] byteBodyContents = request.getByteBodyContents();
        
        assertArrayEquals(expected, byteBodyContents);
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        Verb verb = request.getVerb();
        
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        
        Map<String, String> headers = request.getHeaders();
        
        assertEquals(1, headers.size());
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testGetCharset_Default() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        String charset = request.getCharset();
        
        assertEquals(Charset.defaultCharset().name(), charset);
    }
    
    @Test
    public void testGetCharset_Custom() throws Exception {
        Request request = new Request(Verb.GET, "http://www.example.com");
        String expectedCharset = "UTF-8";
        request.setCharset(expectedCharset);
        
        String charset = request.getCharset();
        
        assertEquals(expectedCharset, charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        
        request.setConnectTimeout(duration, unit);
        
        assertEquals((int)unit.toMillis(duration), request.connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        
        request.setReadTimeout(duration, unit);
        
        assertEquals((int)unit.toMillis(duration), request.connection.getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        String charsetName = "UTF-8";
        
        request.setCharset(charsetName);
        
        assertEquals(charsetName, request.charset);
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.connectionKeepAlive);
    }
    
}