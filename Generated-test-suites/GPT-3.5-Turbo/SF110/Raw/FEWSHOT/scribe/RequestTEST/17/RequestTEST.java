import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("username", "testuser");
        assertEquals("testuser", request.getBodyParams().get("username"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("page", "1");
        assertEquals("1", request.getQueryStringParams().get("page"));
    }

    @Test
    public void testAddPayloadString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }

    @Test
    public void testAddPayloadByteArray() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        byte[] payload = {0, 1, 2};
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        // No assertion, just checking for exceptions
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        // No assertion, just checking for exceptions
    }

    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}