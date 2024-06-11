import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class RequestTest {

    private Request request;

    @Before
    public void setup() {
        request = new Request(Verb.GET, "https://example.com");
    }

    @Test
    public void testAddHeader() {
        // Typical use case
        request.addHeader("Authorization", "Bearer token");
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
        
        // Edge case - empty key and value
        request.addHeader("", "");
        assertNull(request.getHeaders().get(""));
    }

    @Test
    public void testAddBodyParameter() {
        // Typical use case
        request.addBodyParameter("param1", "value1");
        assertEquals("value1", request.getBodyParams().get("param1"));
        
        // Edge case - empty key and value
        request.addBodyParameter("", "");
        assertNull(request.getBodyParams().get(""));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Typical use case
        request.addQuerystringParameter("param1", "value1");
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        
        // Edge case - empty key and value
        request.addQuerystringParameter("", "");
        assertNull(request.getQueryStringParams().get(""));
    }

    @Test
    public void testAddPayload() {
        // Typical use case
        request.addPayload("payload");
        assertEquals("payload", request.getPayload());

        // Edge case - null payload
        request.addPayload(null);
        assertNull(request.getPayload());
    }

    @Test
    public void testAddPayload_byte() {
        // Typical use case
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());

        // Edge case - null payload
        request.addPayload(null);
        assertNull(request.getBytePayload());
    }

    @Test
    public void testSend() {
        // Error scenario - connection exception
        request.setConnectTimeout(1, TimeUnit.MILLISECONDS);
        try {
            request.send();
            fail("Expected exception to be thrown");
        } catch (OAuthException e) {
            assertTrue(e.getMessage().toLowerCase().contains("problems while creating connection"));
        }
    }

    @Test
    public void testSetCharset() {
        // Typical use case
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
        
        // Typical use case - default charset
        request.setCharset(null);
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        // Typical use case
        request.setConnectionKeepAlive(true);
        assertTrue(request.isConnectionKeepAlive());

        // Typical use case
        request.setConnectionKeepAlive(false);
        assertFalse(request.isConnectionKeepAlive());
    }
}