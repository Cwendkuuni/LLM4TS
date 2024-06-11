import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDoSend() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAddHeaders() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addHeader("Authorization", "Bearer Token");
        assertEquals("Bearer Token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testAddBody() {
        // Typical use case
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("param1", "value1");
        try {
            request.createConnection();
            request.addBody(request.connection, request.getByteBodyContents());
            assertNotNull(request.connection.getOutputStream());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAddBodyParameter() {
        // Typical use case
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("value1", request.getBodyParams().get("param1"));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addQuerystringParameter("param1", "value1");
        assertEquals("value1", request.getQueryStringParams().get("param1"));
    }

    @Test
    public void testAddPayload_String() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getByteBodyContents());
    }

    @Test
    public void testAddPayload_Byte() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetQueryStringParams() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        assertEquals(2, request.getQueryStringParams().size());
    }

    @Test
    public void testGetBodyParams() {
        // Typical use case
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals(1, request.getBodyParams().size());
    }

    @Test
    public void testGetUrl() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        assertEquals("https://api.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com:8080");
        assertEquals("https://api.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        // Typical use case
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("param1", "value1");
        assertEquals("param1=value1", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        // Typical use case
        Request request = new Request(Verb.POST, "https://api.example.com");
        request.addBodyParameter("param1", "value1");
        byte[] expected = {112, 97, 114, 97, 109, 49, 61, 118, 97, 108, 117, 101, 49};
        assertArrayEquals(expected, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.addHeader("Authorization", "Bearer Token");
        assertEquals(1, request.getHeaders().size());
    }

    @Test
    public void testGetCharset() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setReadTimeout(2000, TimeUnit.MILLISECONDS);
        assertEquals(2000, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        HttpURLConnection connection = new HttpURLConnection(new URL("https://api.example.com"));
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }

    @Test
    public void testToString() {
        // Typical use case
        Request request = new Request(Verb.GET, "https://api.example.com");
        assertEquals("@Request(GET https://api.example.com)", request.toString());
    }
}