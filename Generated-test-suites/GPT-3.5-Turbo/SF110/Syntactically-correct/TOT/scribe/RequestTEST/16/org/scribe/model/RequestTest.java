import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.scribe.model.*;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "http://example.com");
    }

    @Test
    public void testAddHeader() {
        String key = "Authorization";
        String value = "Bearer TOKEN123";
        request.addHeader(key, value);
        assertEquals(value, request.getHeaders().get(key));
    }

    @Test
    public void testAddBodyParameter() {
        String key = "username";
        String value = "testuser";
        request.addBodyParameter(key, value);
        assertEquals(value, request.getBodyParams().get(key));
    }

    @Test
    public void testAddQuerystringParameter() {
        String key = "page";
        String value = "1";
        request.addQuerystringParameter(key, value);
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    @Test
    public void testAddPayload_String() {
        String payload = "{\"key\":\"value\"}";
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayload_Byte() {
        byte[] payload = "test".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetUrl() {
        String url = "http://example.com";
        request = new Request(Verb.GET, url);
        assertEquals(url, request.getUrl());
    }

    @Test
    public void testGetSanitizedUrl() {
        String url = "http://example.com/test?param=value";
        request = new Request(Verb.GET, url);
        String sanitizedUrl = "http://example.com/test";
        assertEquals(sanitizedUrl, request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        String payload = "{\"key\":\"value\"}";
        request.addPayload(payload);
        assertEquals(payload, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        byte[] payload = "test".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Verb verb = Verb.PUT;
        request = new Request(verb, "http://example.com");
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testGetCharset() {
        assertEquals("UTF-8", request.getCharset());
        request.setCharset("ISO-8859-1");
        assertEquals("ISO-8859-1", request.getCharset());
    }

    @Test
    public void testSetConnectTimeout() {
        int duration = 10;
        request.setConnectTimeout(duration, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(duration * 1000, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        int duration = 10;
        request.setReadTimeout(duration, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(duration * 1000, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        String charset = "ISO-8859-1";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        request.setConnectionKeepAlive(true);
        assertTrue(request.getConnectionKeepAlive());
        request.setConnectionKeepAlive(false);
        assertFalse(request.getConnectionKeepAlive());
    }

}