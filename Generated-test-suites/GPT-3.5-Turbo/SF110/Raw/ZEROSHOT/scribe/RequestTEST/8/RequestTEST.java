import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.*;

public class RequestTest {

    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.addHeaders(conn);
            assertNotNull(conn.getRequestProperty("key"));
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        HttpURLConnection conn = null;
        byte[] content = "test".getBytes();
        try {
            conn = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.addBody(conn, content);
            assertNotNull(conn.getRequestProperty("Content-Length"));
            assertTrue(conn.getDoOutput());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        assertEquals("value", request.getHeaders().get("key"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("key", "value");
        assertEquals("value", request.getQueryStringParams().get("key"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadByte() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertEquals("payload", new String(request.getByteBodyContents()));
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        request.addQuerystringParameter("key2", "value2");
        assertEquals("value", request.getQueryStringParams().get("key"));
        assertEquals("value2", request.getQueryStringParams().get("key2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("http://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?key=value");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addPayload("payload");
        assertEquals("payload", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        byte[] payload = "payload".getBytes();
        request.addPayload(payload);
        assertEquals("payload", new String(request.getByteBodyContents()));
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("key", "value");
        assertEquals("value", request.getHeaders().get("key"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals(Charset.defaultCharset().name(), request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, request.connection.getReadTimeout());
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
        assertTrue(request.connectionKeepAlive);
    }

    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("http://www.example.com").openConnection();
            request.setConnection(conn);
            assertEquals(conn, request.connection);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}