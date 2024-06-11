import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.createConnection();
        assertNotNull(request.connection);
    }

    @Test(expected = OAuthException.class)
    public void testCreateConnectionException() throws IOException {
        Request request = new Request(Verb.GET, "not_a_valid_url");
        request.createConnection();
    }

    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.createConnection();
        Response response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testAddHeaders() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Key", "Value");
        assertEquals("Value", request.headers.get("Key"));
    }

    @Test
    public void testAddBody() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        byte[] content = "Test Content".getBytes();
        request.addBody(request.connection, content);
        assertEquals(content.length, Integer.parseInt(request.connection.getHeaderField("Content-Length")));
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Key", "Value");
        assertEquals("Value", request.headers.get("Key"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("Key", "Value");
        assertEquals("Value", request.bodyParams.get("Key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("Key", "Value");
        assertEquals("Value", request.querystringParams.get("Key"));
    }

    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.payload);
    }

    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.GET, "http://example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.bytePayload);
    }

    @Test
    public void testGetQueryStringParams() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com?key1=value1&key2=value2");
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("key1", "value1");
        expectedParams.put("key2", "value2");
        assertEquals(expectedParams, request.getQueryStringParams());
    }

    @Test(expected = OAuthException.class)
    public void testGetQueryStringParamsException() {
        Request request = new Request(Verb.GET, "not_a_valid_url");
        request.getQueryStringParams();
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "http://example.com");
        Map<String, String> expectedParams = new HashMap<>();
        request.addBodyParameter("Key", "Value");
        assertEquals(expectedParams, request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("http://example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com:8080/path?key=value");
        assertEquals("http://example.com/path", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("Key", "Value");
        assertEquals("Key=Value", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() throws IOException {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("Key", "Value");
        byte[] expectedBytes = "Key=Value".getBytes();
        assertArrayEquals(expectedBytes, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        Map<String, String> expectedHeaders = new HashMap<>();
        assertEquals(expectedHeaders, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(100, TimeUnit.MILLISECONDS);
        assertEquals(100, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(200, TimeUnit.MILLISECONDS);
        assertEquals(200, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = (HttpURLConnection)null;
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("@Request(GET http://example.com)", request.toString());
    }

}