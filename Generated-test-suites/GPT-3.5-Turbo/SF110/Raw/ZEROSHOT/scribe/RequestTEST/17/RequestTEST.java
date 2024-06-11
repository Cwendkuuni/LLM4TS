import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;

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
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("username", "testuser");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("page", "1");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertTrue(queryParams.containsKey("page"));
        assertEquals("1", queryParams.get("page"));
    }
    
    @Test
    public void testAddPayload() {
        request.addPayload("Hello World");
        assertEquals("Hello World", request.getPayload());
    }
    
    @Test
    public void testAddBytePayload() {
        byte[] payload = "Hello World".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        request.addQuerystringParameter("page", "1");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertTrue(queryParams.containsKey("page"));
        assertEquals("1", queryParams.get("page"));
    }
    
    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("username", "testuser");
        Map<String, String> bodyParams = request.getBodyParams();
        assertTrue(bodyParams.containsKey("username"));
        assertEquals("testuser", bodyParams.get("username"));
    }
    
    @Test
    public void testGetUrl() {
        assertEquals("https://example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        assertEquals("https://example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        request.addPayload("Hello World");
        assertEquals("Hello World", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        byte[] payload = "Hello World".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
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
    
    @Test
    public void testSetConnection() {
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        assertEquals("@Request(GET https://example.com)", request.toString());
    }
    
}