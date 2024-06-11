import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.exceptions.OAuthException;
import java.util.*;
import java.io.*;
import java.nio.charset.*;

public class RequestTest {

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.send();
    }
    
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.createConnection();
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] content = baos.toByteArray();
        request.addBody(request.getConnection(), content);
        assertEquals("12", request.getHeaders().get("Content-Length"));
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("key", "value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value", queryStringParams.get("key"));
    }
    
    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] payload = "payload".getBytes(Charset.forName("UTF-8"));
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://example.com?key=value");
        request.addQuerystringParameter("key2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertEquals("value", queryStringParams.get("key"));
        assertEquals("value2", queryStringParams.get("key2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value", bodyParams.get("key"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("http://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?key=value");
        assertEquals("http://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        assertEquals("key=value", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("key", "value");
        byte[] expected = "key=value".getBytes(Charset.forName("UTF-8"));
        assertArrayEquals(expected, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnection().getDoOutput());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = new HttpURLConnection(null);
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("@Request(GET http://example.com)", request.toString());
    }

}