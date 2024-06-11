import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    private final String testUrl = "http://example.com";
    private final Verb testVerb = Verb.GET;
    
    @Test
    public void testSend() {
        Request request = new Request(testVerb, testUrl);
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(testVerb, testUrl);
        String key = "Content-Type";
        String value = "application/json";
        request.addHeader(key, value);
        assertEquals(value, request.getHeaders().get(key));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(testVerb, testUrl);
        String key = "param";
        String value = "value";
        request.addBodyParameter(key, value);
        assertEquals(value, request.getBodyParams().get(key));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(testVerb, testUrl);
        String key = "param";
        String value = "value";
        request.addQuerystringParameter(key, value);
        assertEquals(value, request.getQueryStringParams().get(key));
    }
    
    @Test
    public void testAddPayload_String() {
        Request request = new Request(testVerb, testUrl);
        String payload = "test payload";
        request.addPayload(payload);
        assertEquals(payload, request.getBodyContents());
    }
    
    @Test
    public void testAddPayload_byteArray() {
        Request request = new Request(testVerb, testUrl);
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(testVerb, testUrl);
        assertEquals(testUrl, request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(testVerb, testUrl);
        assertEquals(testUrl, request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(testVerb, testUrl);
        String bodyContents = "";
        try {
            bodyContents = new String(request.getByteBodyContents(), request.getCharset());
        } catch (UnsupportedEncodingException e) {
            fail("Exception thrown while getting body contents: " + e.getMessage());
        }
        assertEquals(bodyContents, request.getBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(testVerb, testUrl);
        assertEquals(testVerb, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(testVerb, testUrl);
        assertNotNull(request.getHeaders());
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(testVerb, testUrl);
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(testVerb, testUrl);
        int timeout = 5000;
        request.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
        assertEquals(timeout, request.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(testVerb, testUrl);
        int timeout = 5000;
        request.setReadTimeout(timeout, TimeUnit.MILLISECONDS);
        assertEquals(timeout, request.getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(testVerb, testUrl);
        String charset = "UTF-8";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(testVerb, testUrl);
        boolean keepAlive = true;
        request.setConnectionKeepAlive(keepAlive);
        assertEquals(keepAlive, request.getConnectionKeepAlive());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(testVerb, testUrl);
        String expectedString = String.format("@Request(%s %s)", request.getVerb(), request.getUrl());
        assertEquals(expectedString, request.toString());
    }

}