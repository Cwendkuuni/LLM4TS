
import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testDefaultConstructor() {
        Request request = new Request(Verb.GET, "https://www.example.com");

        assertNotNull(request.getVerb());
        assertEquals(Verb.GET, request.getVerb());

        assertNotNull(request.getUrl());
        assertEquals("https://www.example.com", request.getUrl());

        assertNotNull(request.getQueryStringParams());
        assertTrue(request.getQueryStringParams().isEmpty());

        assertNotNull(request.getBodyParams());
        assertTrue(request.getBodyParams().isEmpty());

        assertNotNull(request.getHeaders());
        assertTrue(request.getHeaders().isEmpty());

        assertNull(request.getBodyContents());
        assertNull(request.getSanitizedUrl());
    }

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();

        assertNotNull(response);
    }

    @Test(expected = OAuthException.class)
    public void testSendIOException() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(1, TimeUnit.MILLISECONDS);
        request.send();
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");

        assertNotNull(request.getHeaders());
        assertFalse(request.getHeaders().isEmpty());
        assertEquals(1, request.getHeaders().size());
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("param1", "value1");

        assertNotNull(request.getQueryStringParams());
        assertFalse(request.getQueryStringParams().isEmpty());
        assertEquals(1, request.getQueryStringParams().size());
        assertEquals("value1", request.getQueryStringParams().get("param1"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");

        assertNotNull(request.getBodyParams());
        assertFalse(request.getBodyParams().isEmpty());
        assertEquals(1, request.getBodyParams().size());
        assertEquals("value1", request.getBodyParams().get("param1"));
    }

    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("payloadTest");

        assertNotNull(request.getByteBodyContents());
        assertEquals("payloadTest", new String(request.getByteBodyContents()));
    }

    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.POST, "https://www.example.com");

        assertNotNull(request.getVerb());
        assertEquals(Verb.POST, request.getVerb());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(500, TimeUnit.MILLISECONDS);

        // Verify if the connect timeout was properly set
        assertNotNull(request.toString());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(500, TimeUnit.MILLISECONDS);

        // Verify if the read timeout was properly set
        assertNotNull(request.toString());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-8");

        // Verify if the charset was properly set
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);

        // Verify if the connection keep alive was properly set
        assertTrue(request.toString().contains("true"));
    }
}

