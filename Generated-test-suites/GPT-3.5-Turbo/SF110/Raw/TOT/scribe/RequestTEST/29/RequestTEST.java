
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.exceptions.OAuthException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void send_shouldReturnResponse() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.send());
    }

    @Test(expected = OAuthException.class)
    public void send_invalidUrl_shouldThrowException() {
        Request request = new Request(Verb.GET, "");
        request.send();
    }

    @Test
    public void createConnection_connectionKeepAlive_shouldSetHttpKeepAliveProperty() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        request.createConnection();
        assertTrue(Boolean.valueOf(System.getProperty("http.keepAlive")));
    }
    
    @Test
    public void createConnection_noConnection_shouldCreateNewConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.createConnection();
        assertNotNull(request.getConnection());
    }
    
    @Test
    public void doSend_putVerb_shouldSetRequestMethodToPut() {
        Request request = new Request(Verb.PUT, "https://www.example.com");
        request.createConnection();
        request.doSend();
        assertEquals(request.getVerb().name(), request.getConnection().getRequestMethod());
    }
    
    @Test
    public void doSend_postVerb_shouldSetRequestMethodToPost() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.createConnection();
        request.doSend();
        assertEquals(request.getVerb().name(), request.getConnection().getRequestMethod());
    }
    
    @Test
    public void addHeaders_shouldAddHeadersToConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        request.createConnection();
        request.addHeaders(request.getConnection());
        assertNotNull(request.getConnection().getRequestProperty("Content-Type"));
        assertEquals("application/json", request.getConnection().getRequestProperty("Content-Type"));
    }
    
    @Test
    public void addBody_shouldSetContentLengthHeaderAndWriteContentToOutputStream() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] content = "Test Body".getBytes();
        request.createConnection();
        request.addBody(request.getConnection(), content);
        assertNotNull(request.getConnection().getRequestProperty("Content-Length"));
        assertEquals(String.valueOf(content.length), request.getConnection().getRequestProperty("Content-Length"));
    }
    
    @Test
    public void addHeader_shouldAddHeaderToHeadersMap() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Accept", "application/xml");
        assertTrue(request.getHeaders().containsKey("Accept"));
        assertEquals("application/xml", request.getHeaders().get("Accept"));
    }
    
    @Test
    public void addBodyParameter_shouldAddParameterToBodyParamsMap() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addBodyParameter("name", "John Doe");
        assertTrue(request.getBodyParams().containsKey("name"));
        assertEquals("John Doe", request.getBodyParams().get("name"));
    }
    
    @Test
    public void addQuerystringParameter_shouldAddParameterToQuerystringParamsMap() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("page", "1");
        assertTrue(request.getQueryStringParams().containsKey("page"));
        assertEquals("1", request.getQueryStringParams().get("page"));
    }
    
    @Test
    public void addPayload_stringPayload_shouldSetPayload() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addPayload("Test payload");
        assertNotNull(request.getPayload());
        assertEquals("Test payload", request.getPayload());
    }
    
    @Test
    public void addPayload_byteArrayPayload_shouldSetBytePayload() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        byte[] payload = {0x01, 0x02, 0x03};
        request.addPayload(payload);
        assertNotNull(request.getBytePayload());
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void getQueryStringParams_shouldReturnMergedParamsFromUrlAndQuerystringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com?page=1");
        request.addQuerystringParameter("size", "10");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertTrue(queryParams.containsKey("page"));
        assertTrue(queryParams.containsKey("size"));
        assertEquals("1", queryParams.get("page"));
        assertEquals("10", queryParams.get("size"));
    }
    
    @Test
    public void getBodyParams_shouldReturnBodyParams() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addBodyParameter("name", "John Doe");
        request.addBodyParameter("age", "25");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("name"));
        assertTrue(bodyParams.containsKey("age"));
        assertEquals("John Doe", bodyParams.get("name"));
        assertEquals("25", bodyParams.get("age"));
    }
    
    @Test
    public void getUrl_shouldReturnUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getUrl());
    }
    
    @Test
    public void getSanitizedUrl_shouldReturnUrlWithoutQueryStringAndPort() {
        Request request = new Request(Verb.GET, "https://www.example.com:8080/path?page=1");
        assertEquals("https://www.example.com/path", request.getSanitizedUrl());
    }
    
    @Test
    public void getBodyContents_shouldReturnStringRepresentationOfBodyContents() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addBodyParameter("name", "John Doe");
        request.setCharset("UTF-8");
        assertEquals("name=John Doe", request.getBodyContents());
    }
    
    @Test
    public void getByteBodyContents_shouldReturnByteArrayRepresentationOfBodyContents() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addBodyParameter("name", "John Doe");
        byte[] expectedBody = "name=John Doe".getBytes();
        assertArrayEquals(expectedBody, request.getByteBodyContents());
    }
    
    @Test
    public void getVerb_shouldReturnVerb() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void getHeaders_shouldReturnHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/xml");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/xml", headers.get("Content-Type"));
    }
    
    @Test
    public void getCharset_noCharsetSet_shouldReturnDefaultCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.getCharset());
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void setConnectTimeout_shouldSetConnectTimeoutOnConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void setReadTimeout_shouldSetReadTimeoutOnConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void setCharset_shouldSetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void setConnectionKeepAlive_shouldSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
}

