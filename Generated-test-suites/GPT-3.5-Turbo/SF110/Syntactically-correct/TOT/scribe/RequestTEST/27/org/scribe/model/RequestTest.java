import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class RequestTest {

    @Test(expected=OAuthException.class)
    public void testSendWithIOException() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnection(null);
        request.send();
    }

    @Test
    public void testCreateConnection() throws IOException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.createConnection();
        assertNotNull(request.connection);
    }

    @Test
    public void testDoSendWithGet() throws IOException {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }

    @Test
    public void testDoSendWithPut() throws IOException {
        Request request = new Request(Verb.PUT, "https://www.example.com");
        Response response = request.doSend();
        assertNotNull(response);
    }
    
    @Test(expected=UnsupportedEncodingException.class)
    public void testGetBodyContentsWithUnsupportedCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("invalidCharset");
        request.getBodyContents();
    }
    
    @Test
    public void testGetByteBodyContentsWithNullBytePayload() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        byte[] byteContents = request.getByteBodyContents();
        assertNotNull(byteContents);
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.getHeaders());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.getBodyParams());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertNotNull(request.getQueryStringParams());
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }
}