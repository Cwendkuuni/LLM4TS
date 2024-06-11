
import org.junit.Test;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ResponseTest {
    
    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        // Arrange
        HttpURLConnection connection = createMockSuccessfulConnection();
        
        // Act
        Response response = new Response(connection);
        
        // Assert
        assertEquals(200, response.getCode());
        assertFalse(response.getHeaders().isEmpty());
        assertNotNull(response.getStream());
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testConstructor_UnsuccessfulConnection() throws IOException {
        // Arrange
        HttpURLConnection connection = createMockUnsuccessfulConnection();
        
        // Act
        Response response = new Response(connection);
        
        // Assert
        assertEquals(404, response.getCode());
        assertTrue(response.getHeaders().isEmpty());
        assertNull(response.getStream());
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testParseBodyContents_BodyNotNull() {
        // Arrange
        Response response = createMockResponse();
        
        // Act
        String body = response.parseBodyContents();
        
        // Assert
        assertEquals("Mock Response Body", body);
        assertEquals("Mock Response Body", response.getBody());
    }
    
    @Test
    public void testParseBodyContents_BodyNull() {
        // Arrange
        Response response = createMockResponseWithNullBody();
        
        // Act
        String body = response.parseBodyContents();
        
        // Assert
        assertEquals("", body);
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        // Arrange
        HttpURLConnection connection = createMockSuccessfulConnection();
        
        // Act
        Response response = new Response(connection);
        
        // Assert
        assertEquals("Value1", response.getHeader("Header1"));
        assertEquals("Value2", response.getHeader("Header2"));
        assertEquals("Value3", response.getHeader("Header3"));
    }
    
    @Test
    public void testWasSuccessful_Success() {
        // Arrange
        Response response = createMockResponse();
        
        // Act
        boolean wasSuccessful = response.wasSuccessful();
        
        // Assert
        assertTrue(wasSuccessful);
    }
    
    @Test
    public void testWasSuccessful_Failure() {
        // Arrange
        Response response = createMockResponseWithErrorCode();
        
        // Act
        boolean wasSuccessful = response.wasSuccessful();
        
        // Assert
        assertFalse(wasSuccessful);
    }
    
    // Helper Methods
    
    private HttpURLConnection createMockSuccessfulConnection() {
        return new MockHttpURLConnection(200, "Mock Response Body", createMockHeaders());
    }
    
    private HttpURLConnection createMockUnsuccessfulConnection() {
        return new MockHttpURLConnection(404, "", createMockHeaders());
    }
    
    private Response createMockResponse() {
        return new Response(new MockHttpURLConnection(200, "Mock Response Body", createMockHeaders()));
    }
    
    private Response createMockResponseWithNullBody() {
        return new Response(new MockHttpURLConnection(200, null, createMockHeaders()));
    }
    
    private Response createMockResponseWithErrorCode() {
        return new Response(new MockHttpURLConnection(400, "Mock Response Body", createMockHeaders()));
    }
    
    private Map<String, String> createMockHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        headers.put("Header3", "Value3");
        return headers;
    }
    
    private static class MockHttpURLConnection extends HttpURLConnection {
        
        private final int responseCode;
        private final String responseBody;
        private final Map<String, String> headers;
        private final InputStream inputStream;
        
        public MockHttpURLConnection(int responseCode, String responseBody, Map<String, String> headers) {
            super(null);
            this.responseCode = responseCode;
            this.responseBody = responseBody;
            this.headers = headers;
            this.inputStream = (responseBody != null) ? new ByteArrayInputStream(responseBody.getBytes()) : null;
        }
        
        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }

        @Override
        public Map<String, String> getHeaderFields() {
            return headers;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }
        
        @Override
        public InputStream getErrorStream() {
            return inputStream;
        }
        
        @Override
        public void disconnect() {}
        
        @Override
        public void connect() {}
    }
}

