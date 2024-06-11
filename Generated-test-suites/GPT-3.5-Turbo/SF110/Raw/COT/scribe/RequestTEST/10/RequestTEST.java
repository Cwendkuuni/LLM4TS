import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        assertNotNull(request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "testUser");
        assertNotNull(request.getBodyParams().get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("page", "1");
        assertNotNull(request.getQueryStringParams().get("page"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload("Test payload");
        assertEquals("Test payload", request.getBodyContents());
    }

    @Test
    public void testAddPayloadBytes() {
        Request request = new Request(Verb.GET, "http://example.com");
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com:8080/test?param=value");
        assertEquals("http://example.com/test", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "testUser");
        assertEquals("username=testUser", request.getBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertNotNull(request);
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertNotNull(request);
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertNotNull(request.toString());
    }
}