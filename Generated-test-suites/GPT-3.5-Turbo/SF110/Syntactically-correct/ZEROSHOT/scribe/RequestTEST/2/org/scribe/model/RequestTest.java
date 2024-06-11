import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void testConstructor() {
        Verb verb = Verb.GET;
        String url = "https://www.example.com";
        Request request = new Request(verb, url);

        assertEquals(verb, request.getVerb());
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testSend() throws IOException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();

        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "https://invalidurl");
        request.send();
    }

    @Test
    public void testCreateConnection() throws NoSuchFieldException, IllegalAccessException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.createConnection();

        Field connectionField = request.getClass().getDeclaredField("connection");
        connectionField.setAccessible(true);
        Object connection = connectionField.get(request);

        assertNotNull(connection);
    }

    @Test
    public void testDoSend() throws IOException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.doSend();

        assertNotNull(response);
    }

    @Test(expected = IOException.class)
    public void testAddBodyIOException() throws IOException {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBody(null, new byte[]{});
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        assertEquals(headers, request.getHeaders());
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("name", "John");

        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("name", "John");
        assertEquals(bodyParams, request.getBodyParams());
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("page", "1");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "1");
        assertEquals(queryParams, request.getQueryStringParams());
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("payload");

        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testAddBytePayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload(new byte[]{0x01, 0x02, 0x03});

        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, request.getByteBodyContents());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com?param=value");
        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("UTF-8", request.getCharset());

        request.setCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(500, java.util.concurrent.TimeUnit.MILLISECONDS);

        // No assertion, just testing that no exception is thrown
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(500, java.util.concurrent.TimeUnit.MILLISECONDS);

        // No assertion, just testing that no exception is thrown
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);

        // No assertion, just testing that no exception is thrown
    }

    @Test
    public void testSetConnection() throws NoSuchFieldException, IllegalAccessException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));

        request.setConnection(connection);

        Field connectionField = request.getClass().getDeclaredField("connection");
        connectionField.setAccessible(true);
        Object modifiedConnection = connectionField.get(request);

        assertEquals(connection, modifiedConnection);
    }

    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "https://www.example.com";
        Request request = new Request(verb, url);

        assertEquals(String.format("@Request(%s %s)", verb, url), request.toString());
    }
}