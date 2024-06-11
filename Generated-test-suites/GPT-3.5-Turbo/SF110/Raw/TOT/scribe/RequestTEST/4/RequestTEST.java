import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {
    private Request request;
    private HttpURLConnection connection;
    private String url;
    private Verb verb;

    @Before
    public void setUp() {
        url = "https://example.com";
        verb = Verb.GET;
        request = new Request(verb, url);
    }

    @Test
    public void testSend() throws IOException {
        Response response = request.send();
        assertNotNull(response);
        assertEquals(verb, response.getRequest().getVerb());
        assertEquals(url, response.getRequest().getUrl());
    }

    @Test
    public void testCreateConnection() throws IOException {
        request.createConnection();
        connection = request.getConnection();
        assertNotNull(connection);
        assertEquals(url, connection.getURL().toString());
        assertEquals(Verb.GET.name(), connection.getRequestMethod());
    }

    @Test
    public void testDoSend_GET() throws IOException {
        request.createConnection();
        URL reqUrl = new URL(url);
        request.setConnection((HttpURLConnection) reqUrl.openConnection());
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(request.getVerb(), response.getRequest().getVerb());
        assertEquals(reqUrl, response.getRequest().getConnection().getURL());
    }

    @Test
    public void testDoSend_PUT() throws IOException {
        request = new Request(Verb.PUT, url);
        request.createConnection();
        URL reqUrl = new URL(url);
        request.setConnection((HttpURLConnection) reqUrl.openConnection());
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(request.getVerb(), response.getRequest().getVerb());
        assertEquals(reqUrl, response.getRequest().getConnection().getURL());
    }

    @Test
    public void testDoSend_POST() throws IOException {
        request = new Request(Verb.POST, url);
        request.createConnection();
        URL reqUrl = new URL(url);
        request.setConnection((HttpURLConnection) reqUrl.openConnection());
        Response response = request.doSend();
        assertNotNull(response);
        assertEquals(request.getVerb(), response.getRequest().getVerb());
        assertEquals(reqUrl, response.getRequest().getConnection().getURL());
    }

    @Test
    public void testAddHeaders() {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testAddBody() {
        byte[] content = {0x01, 0x02, 0x03};
        try {
            request.addBody(connection, content);
            assertEquals("3", connection.getRequestProperty("Content-Length"));
            assertTrue(connection.getDoOutput());
            assertArrayEquals(content, connection.getOutputStream().toByteArray());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testAddHeader() {
        request.addHeader("Content-Type", "application/json");
        Map<String, String> headers = request.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        Map<String, String> bodyParams = request.getBodyParams();
        assertEquals("value1", bodyParams.get("param1"));
        assertEquals("value2", bodyParams.get("param2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryParams = request.getQueryStringParams();
        assertEquals("value1", queryParams.get("param1"));
        assertEquals("value2", queryParams.get("param2"));
    }

    @Test
    public void testAddPayload_string() {
        String payload = "example payload";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
        assertNull(request.getBytePayload());
    }

    @Test
    public void testAddPayload_byteArray() {
        byte[] payload = {0x01, 0x02, 0x03};
        request.addPayload(payload);
        assertNull(request.getPayload());
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetQueryStringParams() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("param1", "value1");
        queryParams.put("param2", "value2");

        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");

        assertEquals(queryParams, request.getQueryStringParams());
    }

    @Test
    public void testGetBodyParams() {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("param1", "value1");
        bodyParams.put("param2", "value2");

        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        assertEquals(bodyParams, request.getBodyParams());
    }

    @Test
    public void testGetUrl() {
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        String sanitizedUrl = "https://example.com";
        assertEquals(sanitizedUrl, request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        String body = "{\"param1\":\"value1\",\"param2\":\"value2\"}";
        byte[] byteBodyContents = body.getBytes();

        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        assertEquals(body, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        String body = "{\"param1\":\"value1\",\"param2\":\"value2\"}";
        byte[] byteBodyContents = body.getBytes();

        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");

        assertArrayEquals(byteBodyContents, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");

        request.addHeader("Content-Type", "application/json");
        request.addHeader("Authorization", "Bearer token");

        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        int duration = 5000;
        request.setConnectTimeout(duration, TimeUnit.MILLISECONDS);
        assertEquals(duration, connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        int duration = 5000;
        request.setReadTimeout(duration, TimeUnit.MILLISECONDS);
        assertEquals(duration, connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        String charset = "UTF-16";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());

        request.setConnectionKeepAlive(false);
        assertFalse(request.isConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        String expected = String.format("@Request(%s %s)", request.getVerb(), request.getUrl());
        assertEquals(expected, request.toString());
    }
}