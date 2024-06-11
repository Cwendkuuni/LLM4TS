import org.junit.*;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
            assertNotNull(request.connection);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "text/plain");
        try {
            Response response = request.doSend();
            assertNotNull(response);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
    }

    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "text/plain");
        request.addHeader("Authorization", "Bearer token123");
        assertEquals(2, request.headers.size());
        assertEquals("text/plain", request.headers.get("Content-Type"));
        assertEquals("Bearer token123", request.headers.get("Authorization"));
    }

    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBody("Sample Body");
        assertEquals("Sample Body", request.payload);
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "text/plain");
        assertEquals(1, request.headers.size());
        assertEquals("text/plain", request.headers.get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertEquals(2, request.bodyParams.size());
        assertEquals("value1", request.bodyParams.get("param1"));
        assertEquals("value2", request.bodyParams.get("param2"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("param1", "value1");
        request.addQuerystringParameter("param2", "value2");
        assertEquals(2, request.querystringParams.size());
        assertEquals("value1", request.querystringParams.get("param1"));
        assertEquals("value2", request.querystringParams.get("param2"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("Sample Payload");
        assertEquals("Sample Payload", request.payload);
    }

    @Test
    public void testAddPayload_byte() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] payload = "Sample Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.bytePayload);
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com?a=1&b=2");
        request.addQuerystringParameter("c", "3");
        assertEquals(3, request.getQueryStringParams().size());
        assertEquals("1", request.getQueryStringParams().get("a"));
        assertEquals("2", request.getQueryStringParams().get("b"));
        assertEquals("3", request.getQueryStringParams().get("c"));
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertEquals(2, request.getBodyParams().size());
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com:8080/path?query=1");
        assertEquals("https://www.example.com/path", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertEquals("param1=value1&param2=value2", request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        byte[] expected = "param1=value1&param2=value2".getBytes();
        assertArrayEquals(expected, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "text/plain");
        request.addHeader("Authorization", "Bearer token123");
        assertEquals(2, request.getHeaders().size());
        assertEquals("text/plain", request.getHeaders().get("Content-Type"));
        assertEquals("Bearer token123", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNull(request.getCharset());
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.charset);
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection(new URL("https://www.example.com"));
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        String expected = "@Request(GET https://www.example.com)";
        assertEquals(expected, request.toString());
    }
}