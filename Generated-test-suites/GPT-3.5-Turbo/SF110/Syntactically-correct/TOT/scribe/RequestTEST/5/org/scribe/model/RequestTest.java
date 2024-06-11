import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {
    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://example.com");
    }

    // Test send method with successful connection
    @Test
    public void testSend_Success() {
        Response response = request.send();
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
    }

    // Test send method with IOException while creating connection
    @Test(expected = OAuthException.class)
    public void testSend_ConnectionCreationError() {
        request.setUrl("invalid-url");
        request.send();
    }

    // Test createConnection method with successful connection
    @Test
    public void testCreateConnection_Success() throws IOException {
        String expectedUrl = "http://example.com?param1=value1";
        request.addQuerystringParameter("param1", "value1");

        request.createConnection();

        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
        assertEquals(expectedUrl, connection.getURL().toString());
    }

    // Test createConnection method without query string parameters
    @Test
    public void testCreateConnection_NoQuerystringParams() throws IOException {
        request.createConnection();

        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
        assertEquals(request.getUrl(), connection.getURL().toString());
    }

    // Test doSend method with successful connection and GET verb
    @Test
    public void testDoSend_Get_Success() throws IOException {
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
    }

    // Test doSend method with successful connection and PUT verb
    @Test
    public void testDoSend_Put_Success() throws IOException {
        request.setVerb(Verb.PUT);
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
    }

    // Test doSend method with successful connection and POST verb
    @Test
    public void testDoSend_Post_Success() throws IOException {
        request.setVerb(Verb.POST);
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getCode());
    }

    // Test addHeaders method with a single header
    @Test
    public void testAddHeaders_SingleHeader() throws IOException {
        request.addHeader("Content-Type", "application/json");

        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        request.addHeaders(connection);

        String headerValue = connection.getRequestProperty("Content-Type");
        assertNotNull(headerValue);
        assertEquals("application/json", headerValue);
    }

    // Test addHeaders method with multiple headers
    @Test
    public void testAddHeaders_MultipleHeaders() throws IOException {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");

        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        request.addHeaders(connection);

        String contentType = connection.getRequestProperty("Content-Type");
        String authorization = connection.getRequestProperty("Authorization");

        assertNotNull(contentType);
        assertEquals("application/json", contentType);

        assertNotNull(authorization);
        assertEquals("Bearer token", authorization);
    }

    // Test addBody method with non-empty content
    @Test
    public void testAddBody_NonEmptyContent() throws IOException {
        byte[] content = "sample content".getBytes(Charset.defaultCharset());

        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        request.addBody(connection, content);

        String contentLength = connection.getRequestProperty("Content-Length");
        assertNotNull(contentLength);
        assertEquals(String.valueOf(content.length), contentLength);
    }

    // Test addBody method with empty content
    @Test
    public void testAddBody_EmptyContent() throws IOException {
        byte[] content = new byte[0];

        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        request.addBody(connection, content);

        String contentLength = connection.getRequestProperty("Content-Length");
        assertNotNull(contentLength);
        assertEquals("0", contentLength);
    }

    // Test addHeader method with a new header
    @Test
    public void testAddHeader_NewHeader() {
        request.addHeader("Content-Type", "application/json");

        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey("Content-Type"));
        assertEquals("application/json", headers.get("Content-Type"));
    }

    // Test addBodyParameter method with a new parameter
    @Test
    public void testAddBodyParameter_NewParameter() {
        request.addBodyParameter("param1", "value1");

        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertTrue(bodyParams.containsKey("param1"));
        assertEquals("value1", bodyParams.get("param1"));
    }

    // Test addQuerystringParameter method with a new parameter
    @Test
    public void testAddQuerystringParameter_NewParameter() {
        request.addQuerystringParameter("param1", "value1");

        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertTrue(queryStringParams.containsKey("param1"));
        assertEquals("value1", queryStringParams.get("param1"));
    }

    // Test addPayload method with a payload string
    @Test
    public void testAddPayload_StringPayload() {
        String payload = "Sample Payload";

        request.addPayload(payload);

        assertEquals(payload, request.getPayload());
    }

    // Test addPayload method with a payload byte array
    @Test
    public void testAddPayload_ByteArrayPayload() {
        byte[] payload = "Sample Payload".getBytes(Charset.defaultCharset());

        request.addPayload(payload);

        assertArrayEquals(payload, request.getBytePayload());
    }

    // Test getQueryStringParams method with query string in the URL and additional query string parameters
    @Test
    public void testGetQueryStringParams_WithQueryString() throws MalformedURLException {
        String url = "http://example.com?param1=value1";
        request.setUrl(url);
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals(2, queryStringParams.size());
        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    // Test getQueryStringParams method with only query string parameters
    @Test
    public void testGetQueryStringParams_OnlyQueryStringParams() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals(2, queryStringParams.size());
        assertTrue(queryStringParams.containsKey("param1"));
        assertTrue(queryStringParams.containsKey("param2"));
        assertEquals("value1", queryStringParams.get("param1"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    // Test getBodyParams method with body parameters
    @Test
    public void testGetBodyParams() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals(2, bodyParams.size());
        assertTrue(bodyParams.containsKey("param1"));
        assertTrue(bodyParams.containsKey("param2"));
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    // Test getSanitizedUrl method with query string in the URL
    @Test
    public void testGetSanitizedUrl_WithQueryString() {
        String url = "http://example.com?param1=value1";
        request.setUrl(url);

        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    // Test getSanitizedUrl method with port number in the URL
    @Test
    public void testGetSanitizedUrl_WithPortNumber() {
        String url = "http://example.com:8080";
        request.setUrl(url);

        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    // Test getSanitizedUrl method with query string and port number in the URL
    @Test
    public void testGetSanitizedUrl_WithQueryStringAndPortNumber() {
        String url = "http://example.com:8080?param1=value1";
        request.setUrl(url);

        assertEquals("http://example.com", request.getSanitizedUrl());
    }

    // Test getBodyContents method with valid charset
    @Test
    public void testGetBodyContents_ValidCharset() {
        String payload = "Sample Payload";
        request.addPayload(payload);
        request.setCharset("UTF-8");

        assertEquals(payload, request.getBodyContents());
    }

    // Test getByteBodyContents method with valid charset
    @Test
    public void testGetByteBodyContents_ValidCharset() {
        String payload = "Sample Payload";
        request.addPayload(payload);
        request.setCharset("UTF-8");

        byte[] expected = payload.getBytes(Charset.forName("UTF-8"));

        assertArrayEquals(expected, request.getByteBodyContents());
    }

    // Test getByteBodyContents method without payload
    @Test
    public void testGetByteBodyContents_NoPayload() {
        byte[] expected = new byte[0];

        assertArrayEquals(expected, request.getByteBodyContents());
    }

    // Test getVerb method
    @Test
    public void testGetVerb() {
        assertEquals(Verb.GET, request.getVerb());
    }

    // Test getHeaders method
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = request.getHeaders();

        assertNotNull(headers);
        assertTrue(headers.isEmpty());
    }

    // Test getCharset method with default charset
    @Test
    public void testGetCharset_DefaultCharset() {
        String defaultCharset = Charset.defaultCharset().name();

        assertEquals(defaultCharset, request.getCharset());
    }

    // Test getCharset method with custom charset
    @Test
    public void testGetCharset_CustomCharset() {
        String charsetName = "UTF-8";
        request.setCharset(charsetName);

        assertEquals(charsetName, request.getCharset());
    }

    // Test setConnectTimeout method
    @Test
    public void testSetConnectTimeout() {
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;

        request.setConnectTimeout(duration, unit);

        assertEquals(unit.toMillis(duration), request.getConnection().getConnectTimeout());
    }

    // Test setReadTimeout method
    @Test
    public void testSetReadTimeout() {
        int duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;

        request.setReadTimeout(duration, unit);

        assertEquals(unit.toMillis(duration), request.getConnection().getReadTimeout());
    }

    // Test setCharset method
    @Test
    public void testSetCharset() {
        String charsetName = "UTF-8";

        request.setCharset(charsetName);

        assertEquals(charsetName, request.getCharset());
    }

    // Test setConnectionKeepAlive method with keep alive enabled
    @Test
    public void testSetConnectionKeepAlive_Enable() {
        request.setConnectionKeepAlive(true);

        assertTrue(request.isConnectionKeepAlive());
    }

    // Test setConnectionKeepAlive method with keep alive disabled
    @Test
    public void testSetConnectionKeepAlive_Disable() {
        request.setConnectionKeepAlive(false);

        assertFalse(request.isConnectionKeepAlive());
    }
}