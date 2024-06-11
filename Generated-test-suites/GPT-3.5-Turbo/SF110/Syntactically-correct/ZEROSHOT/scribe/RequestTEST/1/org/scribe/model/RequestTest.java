import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.exceptions.*;
import org.scribe.utils.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.util.concurrent.*;

public class RequestTest {
    private Request request;
    
    @Before
    public void setUp() {
        String url = "https://www.example.com";
        Verb verb = Verb.GET;
        request = new Request(verb, url);
    }
    
    @Test
    public void testSend() {
        try {
            Response response = request.send();
            assertNotNull(response);
        }
        catch (OAuthException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateConnection() {
        try {
            request.createConnection();
            HttpURLConnection connection = request.getConnection();
            assertNotNull(connection);
            assertEquals(Verb.GET.name(), connection.getRequestMethod());
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSend() {
        try {
            Response response = request.doSend();
            assertNotNull(response);
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeaders() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://www.example.com").openConnection();
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        request.addHeaders(conn);
        assertNotNull(conn.getRequestProperty("Content-Length"));
    }
    
    @Test
    public void testAddBody() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("https://www.example.com").openConnection();
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        byte[] content = "test".getBytes();
        try {
            request.addBody(conn, content);
            assertNotNull(conn.getRequestProperty("Content-Length"));
            assertTrue(conn.getDoOutput());
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeader() {
        String key = "Key";
        String value = "Value";
        request.addHeader(key, value);
        assertEquals(value, request.getHeaders().get(key));
    }
    
    @Test
    public void testAddBodyParameter() {
        String key = "Key";
        String value = "Value";
        request.addBodyParameter(key, value);
        assertEquals(value, request.getBodyParams().get(key));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        String key = "Key";
        String value = "Value";
        request.addQuerystringParameter(key, value);
        assertEquals(value, request.getQueryStringParams().get(key));
    }
    
    @Test
    public void testAddPayload() {
        String payload = "Payload";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }
    
    @Test
    public void testAddPayloadByte() {
        byte[] payload = "Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Map<String, String> query = new HashMap<>();
        query.put("param1", "value1");
        query.put("param2", "value2");
        request.addQuerystringParameter("param3", "value3");
        assertEquals(query, request.getQueryStringParams());
    }
    
    @Test
    public void testGetBodyParams() {
        Map<String, String> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");
        request.addBodyParameter("param3", "value3");
        assertEquals(body, request.getBodyParams());
    }
    
    @Test
    public void testGetUrl() {
        String url = "https://www.example.com";
        assertEquals(url, request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        String url = "https://www.example.com";
        assertEquals(url, request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        String body = "Body";
        request.addPayload(body);
        assertEquals(body, request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        byte[] body = "Body".getBytes();
        request.addPayload(body);
        assertArrayEquals(body, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Verb verb = Verb.GET;
        assertEquals(verb, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("param1", "value1");
        headers.put("param2", "value2");
        request.addHeader("param3", "value3");
        assertEquals(headers, request.getHeaders());
    }
    
    @Test
    public void testGetCharset() {
        String charset = Charset.defaultCharset().name();
        assertEquals(charset, request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setConnectTimeout(duration, unit);
        assertEquals(duration, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        int duration = 5000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        request.setReadTimeout(duration, unit);
        assertEquals(duration, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        String charset = "UTF-8";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
    
    @Test
    public void testSetConnection() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://www.example.com").openConnection();
        }
        catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }
    
    @Test
    public void testToString() {
        String verb = Verb.GET.name();
        String url = "https://www.example.com";
        assertEquals("@Request(" + verb + " " + url + ")", request.toString());
    }
}