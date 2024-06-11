import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void testSend() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        Response response = request.send();
        
        // Assert
        assertNotNull(response);
    }
    
    @Test
    public void testCreateConnection() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        try {
            request.createConnection();
        } catch (Exception e) {
            
        }
        
        // Assert
        assertNotNull(request.getConnection());
    }
    
    @Test
    public void testDoSend() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        
        // Act
        try {
            Response response = request.doSend();
            
            // Assert
            assertNotNull(response);
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void testAddHeader() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.addHeader("Content-Type", "application/json");
        
        // Assert
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }
    
    @Test
    public void testAddBodyParameter() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        
        // Act
        request.addBodyParameter("username", "testuser");
        request.addBodyParameter("password", "testpassword");
        
        // Assert
        assertEquals("testuser", request.getBodyParams().get("username"));
        assertEquals("testpassword", request.getBodyParams().get("password"));
    }
    
    @Test
    public void testAddQuerystringParameter() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.addQuerystringParameter("page", "1");
        request.addQuerystringParameter("limit", "10");
        
        // Assert
        assertEquals("1", request.getQueryStringParams().get("page"));
        assertEquals("10", request.getQueryStringParams().get("limit"));
    }
    
    @Test
    public void testAddPayloadString() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        
        // Act
        request.addPayload("This is a test payload.");
        
        // Assert
        assertEquals("This is a test payload.", request.getPayload());
    }
    
    @Test
    public void testAddPayloadByte() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        
        // Act
        byte[] payload = { 0x01, 0x02, 0x03 };
        request.addPayload(payload);
        
        // Assert
        assertArrayEquals(payload, request.getBytePayload());
    }
    
    @Test
    public void testGetQueryStringParams() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com?page=1&limit=10");
        
        // Act
        Map<String, String> queryStringParams = request.getQueryStringParams();
        
        // Assert
        assertEquals("1", queryStringParams.get("page"));
        assertEquals("10", queryStringParams.get("limit"));
    }
    
    @Test
    public void testGetBodyParams() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "testuser");
        request.addBodyParameter("password", "testpassword");
        
        // Act
        Map<String, String> bodyParams = request.getBodyParams();
        
        // Assert
        assertEquals("testuser", bodyParams.get("username"));
        assertEquals("testpassword", bodyParams.get("password"));
    }
    
    @Test
    public void testGetUrl() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        String url = request.getUrl();
        
        // Assert
        assertEquals("https://www.example.com", url);
    }
    
    @Test
    public void testGetSanitizedUrl() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com?page=1");
        
        // Act
        String sanitizedUrl = request.getSanitizedUrl();
        
        // Assert
        assertEquals("https://www.example.com", sanitizedUrl);
    }
    
    @Test
    public void testGetBodyContents() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("username", "testuser");
        request.addBodyParameter("password", "testpassword");
        
        // Act
        String bodyContents = request.getBodyContents();
        
        // Assert
        assertEquals("username=testuser&password=testpassword", bodyContents);
    }
    
    @Test
    public void testGetByteBodyContents() {
        // Arrange
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addPayload("This is a test payload.");
        
        // Act
        byte[] byteBodyContents = request.getByteBodyContents();
        
        // Assert
        assertArrayEquals("This is a test payload.".getBytes(), byteBodyContents);
    }
    
    @Test
    public void testGetVerb() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        Verb verb = request.getVerb();
        
        // Assert
        assertEquals(Verb.GET, verb);
    }
    
    @Test
    public void testGetHeaders() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Authorization", "Bearer token");
        
        // Act
        Map<String, String> headers = request.getHeaders();
        
        // Assert
        assertEquals("Bearer token", headers.get("Authorization"));
    }
    
    @Test
    public void testGetCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.setCharset("UTF-8");
        
        // Act
        String charset = request.getCharset();
        
        // Assert
        assertEquals("UTF-8", charset);
    }
    
    @Test
    public void testSetConnectTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        
        // Assert
        assertEquals(5000, request.getConnection().getConnectTimeout());
    }
    
    @Test
    public void testSetReadTimeout() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        
        // Assert
        assertEquals(10000, request.getConnection().getReadTimeout());
    }
    
    @Test
    public void testSetCharset() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.setCharset("UTF-8");
        
        // Assert
        assertEquals("UTF-8", request.getCharset());
    }
    
    @Test
    public void testSetConnectionKeepAlive() {
        // Arrange
        Request request = new Request(Verb.GET, "https://www.example.com");
        
        // Act
        request.setConnectionKeepAlive(true);
        
        // Assert
        assertTrue(request.isConnectionKeepAlive());
    }

}