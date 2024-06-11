import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.exceptions.OAuthException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://example.com");
        Response response = request.send();
        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendWithIOException() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnection(null); // Force a NullPointerException
        request.send();
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals(request.getHeaders().get("Content-Type"), "application/json");
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "john");
        assertEquals(request.getBodyParams().get("username"), "john");
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addQuerystringParameter("page", "1");
        assertEquals(request.getQueryStringParams().get("page"), "1");
    }

    @Test
    public void testAddPayloadWithString() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload("payload");
        assertEquals(request.getPayload(), "payload");
    }

    @Test
    public void testAddPayloadWithByteArray() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addPayload(new byte[]{1, 2, 3});
        assertArrayEquals(request.getBytePayload(), new byte[]{1, 2, 3});
    }

    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://example.com?page=1");
        request.addQuerystringParameter("limit", "10");
        assertEquals(request.getQueryStringParams().size(), 2);
        assertEquals(request.getQueryStringParams().get("page"), "1");
        assertEquals(request.getQueryStringParams().get("limit"), "10");
    }

    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "john");
        assertEquals(request.getBodyParams().size(), 1);
        assertEquals(request.getBodyParams().get("username"), "john");
    }

    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(request.getUrl(), "http://example.com");
    }

    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://example.com?url_param=1");
        assertEquals(request.getSanitizedUrl(), "http://example.com");
    }

    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "john");
        assertEquals(request.getBodyContents(), "username=john");
    }

    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addBodyParameter("username", "john");
        assertArrayEquals(request.getByteBodyContents(), "username=john".getBytes());
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(request.getVerb(), Verb.GET);
    }

    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals(request.getHeaders().size(), 1);
        assertEquals(request.getHeaders().get("Content-Type"), "application/json");
    }

    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        assertEquals(request.getCharset(), "UTF-8");
    }

    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        // No easy way to test this, checking for any exceptions thrown
    }

    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        // No easy way to test this, checking for any exceptions thrown
    }

    @Test
    public void testsetCharset() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setCharset("UTF-16");
        assertEquals(request.getCharset(), "UTF-16");
    }

    @Test
    public void testsetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "http://example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
    }
}

