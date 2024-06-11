import org.junit.Test;
import org.scribe.model.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    // Test send() method
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    // Test createConnection() method
    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.createConnection();
        assertNotNull(request.getConnection());
    }

    // Test doSend() method
    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }

    // Test addHeaders() method
    @Test
    public void testAddHeaders() throws IOException {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = (HttpURLConnection) request.getUrl().openConnection();
        request.addHeaders(connection);
        assertNotNull(connection.getRequestProperty("Content-Length"));
    }

    // Test addBody() method
    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.POST, "http://www.example.com");
        HttpURLConnection connection = (HttpURLConnection) request.getUrl().openConnection();
        byte[] content = "test".getBytes(Charset.defaultCharset());
        request.addBody(connection, content);
        assertNotNull(connection.getRequestProperty("Content-Length"));
        assertNotNull(connection.getOutputStream());
    }

    // Test addHeader() method
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        assertTrue(request.getHeaders().containsKey("key"));
    }

    // Test addBodyParameter() method
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertTrue(request.getBodyParams().containsKey("key"));
    }

    // Test addQuerystringParameter() method
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("key", "value");
        assertTrue(request.getQueryStringParams().containsKey("key"));
    }

    // Test addPayload() method with String parameter
    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        String payload = "test";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    // Test addPayload() method with byte[] parameter
    @Test
    public void testAddPayloadBytes() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = "test".getBytes(Charset.defaultCharset());
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    // Test getQueryStringParams() method
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?key1=value1");
        request.addQuerystringParameter("key2", "value2");
        Map<String, String> params = request.getQueryStringParams();
        assertEquals("value1", params.get("key1"));
        assertEquals("value2", params.get("key2"));
    }

    // Test getBodyParams() method
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertTrue(request.getBodyParams().containsKey("key"));
    }

    // Test getUrl() method
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }

    // Test getSanitizedUrl() method
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }

    // Test getBodyContents() method
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertEquals("key=value", request.getBodyContents());
    }

    // Test getByteBodyContents() method
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("key", "value");
        byte[] body = request.getByteBodyContents();
        assertEquals("key=value", new String(body, Charset.defaultCharset()));
    }

    // Test getVerb() method
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    // Test getHeaders() method
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        assertTrue(request.getHeaders().containsKey("key"));
    }

    // Test getCharset() method
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    // Test setConnectTimeout() method
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(10, TimeUnit.SECONDS);
        // No assertion, just checking if the method runs without errors
    }

    // Test setReadTimeout() method
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(10, TimeUnit.SECONDS);
        // No assertion, just checking if the method runs without errors
    }

    // Test setCharset() method
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    // Test setConnectionKeepAlive() method
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }

    // Test setConnection() method
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection connection = (HttpURLConnection) request.getUrl().openConnection();
        request.setConnection(connection);
        assertEquals(connection, request.getConnection());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }

    // Test getContentLength() method
    @Test
    public void testGetContentLength() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("Content-Length", Request.CONTENT_LENGTH);
    }

    // Test connectionKeepAlive property
    @Test
    public void testConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());
    }
}

