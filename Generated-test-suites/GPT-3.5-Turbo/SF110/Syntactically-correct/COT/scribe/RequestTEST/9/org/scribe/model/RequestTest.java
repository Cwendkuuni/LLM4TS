
import org.junit.Test;
import org.scribe.model.Verb;
import org.scribe.model.Request;
import org.scribe.model.Response;

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
        request.addHeader("Authorization", "Bearer token");
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addBodyParameter("username", "test");
        assertEquals("test", request.getBodyParams().get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("page", "1");
        request.addQuerystringParameter("limit", "10");
        assertEquals("1", request.getQueryStringParams().get("page"));
        assertEquals("10", request.getQueryStringParams().get("limit"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testAddPayload_inBytes() {
        Request request = new Request(Verb.POST, "http://example.com");
        byte[] payload = {1, 2, 3};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(Verb.GET, request.getVerb());
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        // Timeout should be set here, no assertion needed
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        // Timeout should be set here, no assertion needed
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getHeaders().containsKey("Connection"));
        assertEquals("keep-alive", request.getHeaders().get("Connection"));
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals("@Request(GET http://example.com)", request.toString());
    }
}
