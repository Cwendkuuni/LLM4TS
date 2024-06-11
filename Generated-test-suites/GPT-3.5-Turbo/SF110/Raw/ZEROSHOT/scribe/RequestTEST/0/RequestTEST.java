import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Request;
import org.scribe.model.Verb;
import org.scribe.model.Response;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        Response response = request.send();
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        try {
            request.createConnection();
            Response response = request.doSend();
            assertNotNull(response);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBody() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] bodyContent = "Test Body".getBytes();
        try {
            request.createConnection();
            request.addBody(request.connection, bodyContent);
            assertEquals(String.valueOf(bodyContent.length), request.connection.getRequestProperty("Content-Length"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Authorization", "Bearer token");
        assertEquals("Bearer token", request.getHeaders().get("Authorization"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param", "value");
        assertEquals("value", request.getBodyParams().get("param"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("param", "value");
        assertEquals("value", request.getQueryStringParams().get("param"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("Test Payload");
        assertEquals("Test Payload", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadAsByte() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        byte[] payload = "Test Payload".getBytes();
        request.addPayload(payload);
        assertArrayEquals(payload, request.getByteBodyContents());
    }
    
    @Test
    public void testGetQueryStringParams() {
        Request request = new Request(Verb.GET, "https://www.example.com?param1=value1");
        request.addQuerystringParameter("param2", "value2");
        assertEquals("value1", request.getQueryStringParams().get("param1"));
        assertEquals("value2", request.getQueryStringParams().get("param2"));
    }
    
    @Test
    public void testGetBodyParams() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertEquals("value1", request.getBodyParams().get("param1"));
        assertEquals("value2", request.getBodyParams().get("param2"));
    }
    
    @Test
    public void testGetUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getUrl());
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("https://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        assertEquals("param1=value1&param2=value2", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("param1", "value1");
        request.addBodyParameter("param2", "value2");
        byte[] bodyContent = "param1=value1&param2=value2".getBytes();
        assertArrayEquals(bodyContent, request.getByteBodyContents());
    }
    
    @Test
    public void testGetVerb() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
    }
    
    @Test
    public void testGetHeaders() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-16");
        assertEquals("UTF-16", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setConnectionKeepAlive(true);
        assertTrue(request.connectionKeepAlive);
    }
    
    @Test
    public void testSetConnection() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals("@Request(GET https://www.example.com)", request.toString());
    }
}