import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RequestTest {
    
    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
        assertNotNull(request.getQueryStringParams());
        assertNotNull(request.getBodyParams());
        assertNotNull(request.getHeaders());
    }
    
    @Test
    public void testSend() throws IOException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        
        assertNotNull(response);
    }
    
    @Test(expected = OAuthException.class)
    public void testSendIOException() throws IOException {
        Request request = new Request(Verb.GET, "https://www.invalidurl.com");
        request.send();
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Authorization", "Bearer token");
        
        assertTrue(request.getHeaders().containsKey("Authorization"));
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "john");
        
        assertTrue(request.getBodyParams().containsKey("username"));
        assertEquals("john", request.getBodyParams().get("username"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("page", "1");
        
        assertTrue(request.getQueryStringParams().containsKey("page"));
        assertEquals("1", request.getQueryStringParams().get("page"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("This is a payload");
        
        assertEquals("This is a payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload(new byte[]{1, 2, 3});
        
        byte[] payload = request.getByteBodyContents();
        assertNotNull(payload);
        assertEquals(3, payload.length);
        assertArrayEquals(new byte[]{1, 2, 3}, payload);
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com?param1=value1&param2=value2");
        
        Map<String, String> params = request.getQueryStringParams();
        assertFalse(params.isEmpty());
        assertTrue(params.containsKey("param1"));
        assertTrue(params.containsKey("param2"));
        assertEquals("value1", params.get("param1"));
        assertEquals("value2", params.get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "john");
        
        Map<String, String> params = request.getBodyParams();
        assertFalse(params.isEmpty());
        assertTrue(params.containsKey("username"));
        assertEquals("john", params.get("username"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertEquals("https://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com?p=1");
        
        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "john");
        
        assertEquals("username=john", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "john");
        
        byte[] payload = request.getByteBodyContents();
        assertNotNull(payload);
        assertEquals(16, payload.length);
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Authorization", "Bearer token");
        
        Map<String, String> headers = request.getHeaders();
        assertFalse(headers.isEmpty());
        assertTrue(headers.containsKey("Authorization"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertNotNull(request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        
        assertEquals(10000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-8");
        
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        assertEquals("@Request(GET https://www.example.com)", request.toString());
    }
}