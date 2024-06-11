import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        Response response = request.send();
        assertNotNull(response); 
    }

    @Test
    public void testAddHeader() {
        String key = "Authorization";
        String value = "Bearer token";
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        request.addHeader(key, value);
        assertTrue(request.getHeaders().containsKey(key));
        assertEquals(value, request.getHeaders().get(key));
    }

    @Test
    public void testAddBodyParameter() {
        String key = "param";
        String value = "value";
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        request.addBodyParameter(key, value);
        assertTrue(request.getBodyParams().containsKey(key));
        assertEquals(value, request.getBodyParams().get(key));
    }

    @Test
    public void testAddQuerystringParameter() {
        String key = "param";
        String value = "value";
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        request.addQuerystringParameter(key, value);
        assertTrue(request.getQueryStringParams().containsKey(key));
        assertEquals(value, request.getQueryStringParams().get(key));
    }

    @Test
    public void testAddPayload() {
        String payload = "payload";
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        request.addPayload(payload);
        assertEquals(payload, request.getPayload());
    }

    @Test
    public void testAddPayloadByteArray() {
        byte[] payload = {0x01, 0x02, 0x03};
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        request.addPayload(payload);
        assertArrayEquals(payload, request.getBytePayload());
    }

    @Test
    public void testGetSanitizedUrl() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com?param=value";
        Request request = new Request(verb, url);
        assertEquals("https://api.example.com", request.getSanitizedUrl());
    }

    @Test
    public void testGetBodyContents() {
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        String bodycontents = "param=value";
        request.addBodyParameter("param", "value");
        assertEquals(bodycontents, request.getBodyContents());
    }

    @Test
    public void testGetByteBodyContents() {
        Verb verb = Verb.POST;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        byte[] byteBodyContents = {0x70, 0x61, 0x72, 0x61, 0x6d, 0x3d, 0x76, 0x61, 0x6c, 0x75, 0x65};
        request.addBodyParameter("param", "value");
        assertArrayEquals(byteBodyContents, request.getByteBodyContents());
    }

    @Test
    public void testGetVerb() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        assertEquals(verb, request.getVerb());
    }

    @Test
    public void testSetConnectTimeout() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        int duration = 5000;
        request.setConnectTimeout(duration, TimeUnit.MILLISECONDS);
        assertEquals(duration, request.getConnection().getConnectTimeout());
    }

    @Test
    public void testSetReadTimeout() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        int duration = 5000;
        request.setReadTimeout(duration, TimeUnit.MILLISECONDS);
        assertEquals(duration, request.getConnection().getReadTimeout());
    }

    @Test
    public void testSetCharset() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        String charset = "UTF-8";
        request.setCharset(charset);
        assertEquals(charset, request.getCharset());
    }

    @Test
    public void testSetConnectionKeepAlive() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        boolean connectionKeepAlive = true;
        request.setConnectionKeepAlive(connectionKeepAlive);
        assertEquals(connectionKeepAlive, request.getConnectionKeepAlive());
    }

    @Test
    public void testToString() {
        Verb verb = Verb.GET;
        String url = "https://api.example.com";
        Request request = new Request(verb, url);
        String expected = String.format("@Request(%s %s)", verb, url);
        assertEquals(expected, request.toString());
    }
}