import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "http://example.com");
        try {
            request.createConnection();
        } catch (IOException e) {
            fail("Failed to create connection");
        }
        assertNotNull(request);
    }

    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        try {
            request.createConnection();
        } catch (IOException e) {
            fail("Failed to create connection");
        }
        try {
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException e) {
            fail("Failed to send request");
        }
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        } catch (IOException e) {
            fail("Failed to create connection");
        }
        request.addHeaders(connection);
        assertNotNull(connection);
    }

    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "http://example.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        } catch (IOException e) {
            fail("Failed to create connection");
        }
        byte[] payload = "test payload".getBytes();
        try {
            request.addBody(connection, payload);
        } catch (IOException e) {
            fail("Failed to add body");
        }
        assertNotNull(connection);
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals("value", bodyParams.get("param"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com?param=value");
        request.addQuerystringParameter("param2", "value2");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals("value", queryStringParams.get("param"));
        assertEquals("value2", queryStringParams.get("param2"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("test payload");
        String payload = request.getBodyContents();
        assertNotNull(payload);
        assertEquals("test payload", payload);
    }

    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] bytePayload = "test payload".getBytes();
        request.addPayload(bytePayload);
        byte[] payload = request.getByteBodyContents();
        assertNotNull(payload);
        assertArrayEquals(bytePayload, payload);
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://example.com?param=value");
        Map<String, String> queryStringParams = request.getQueryStringParams();
        assertNotNull(queryStringParams);
        assertEquals("value", queryStringParams.get("param"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("param", "value");
        Map<String, String> bodyParams = request.getBodyParams();
        assertNotNull(bodyParams);
        assertEquals("value", bodyParams.get("param"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        String url = request.getUrl();
        assertNotNull(url);
        assertEquals("http://example.com", url);
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com:8080?param=value");
        String sanitizedUrl = request.getSanitizedUrl();
        assertNotNull(sanitizedUrl);
        assertEquals("http://example.com", sanitizedUrl);
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("test payload");
        String bodyContents = request.getBodyContents();
        assertNotNull(bodyContents);
        assertEquals("test payload", bodyContents);
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] bytePayload = "test payload".getBytes();
        request.addPayload(bytePayload);
        byte[] bodyContents = request.getByteBodyContents();
        assertNotNull(bodyContents);
        assertArrayEquals(bytePayload, bodyContents);
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        Verb verb = request.getVerb();
        assertNotNull(verb);
        assertEquals(Verb.GET, verb);
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Authorization", "Bearer token");
        Map<String, String> headers = request.getHeaders();
        assertNotNull(headers);
        assertEquals("Bearer token", headers.get("Authorization"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        String charset = request.getCharset();
        assertNotNull(charset);
        assertEquals(Charset.defaultCharset().name(), charset);
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertNotNull(request);
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertNotNull(request);
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        assertNotNull(request);
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertNotNull(request);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://example.com");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        } catch (IOException e) {
            fail("Failed to create connection");
        }
        request.setConnection(connection);
        assertNotNull(request);
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        String toString = request.toString();
        assertNotNull(toString);
    }

}