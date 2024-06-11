import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RequestTest {
    
    @Test
    public void testSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        Response response = request.send();
        assertEquals(200, response.getCode());
        assertEquals("OK", response.getMessage());
    }
    
    @Test
    public void testCreateConnection() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(request.connection);
    }
    
    @Test
    public void testDoSend() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        try {
            request.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Response response = null;
        try {
            response = request.doSend();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(response);
    }
    
    @Test
    public void testAddHeaders() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addBodyParameter("username", "john");
        assertEquals("john", request.getBodyParams().get("username"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("page", "1");
        assertEquals("1", request.getQueryStringParams().get("page"));
    }
    
    @Test
    public void testAddPayload() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("Hello World");
        assertEquals("Hello World", request.getBodyContents());
    }
    
    @Test
    public void testAddPayloadBytes() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("Hello World".getBytes());
        assertEquals("Hello World", new String(request.getByteBodyContents()));
    }
    
    @Test
    public void testGetSanitizedUrl() {
        Request request = new Request(Verb.GET, "http://www.example.com?param=value");
        assertEquals("http://www.example.com", request.getSanitizedUrl());
    }
    
    @Test
    public void testGetBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("Hello World");
        assertEquals("Hello World", request.getBodyContents());
    }
    
    @Test
    public void testGetByteBodyContents() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addPayload("Hello World".getBytes());
        assertEquals("Hello World", new String(request.getByteBodyContents()));
    }
    
    @Test
    public void testGetCharset() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setCharset("UTF-8");
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.setReadTimeout(5000, TimeUnit.MILLISECONDS);
        assertEquals(5000, request.connection.getReadTimeout());
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
        HttpURLConnection connection = new HttpURLConnection();
        request.setConnection(connection);
        assertEquals(connection, request.connection);
    }
    
    @Test
    public void testToString() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertEquals("@Request(GET http://www.example.com)", request.toString());
    }
}