import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UnknownHostException;

public class ResponseTest {

    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(true, 200);
        
        // Act
        Response response = new Response(mockConnection);
        
        // Assert
        assertEquals(mockConnection.getResponseCode(), response.getCode());
        assertNotNull(response.getStream());
        assertEquals(mockConnection.getHeaderFields(), response.getHeaders());
        assertNull(response.getBody());
    }
    
    @Test
    public void testConstructor_UnsuccessfulConnection() throws IOException {
        // Arrange
        HttpURLConnection mockConnection = createMockConnection(false, 404);
        
        // Act
        Response response = new Response(mockConnection);
        
        // Assert
        assertEquals(404, response.getCode());
        assertNull(response.getStream());
        assertEquals(new HashMap<String, String>(), response.getHeaders());
        assertNull(response.getBody());
    }
    
    @Test
    public void testGetBody_ExistingBody() throws IOException {
        // Arrange
        String sampleBody = "Sample body text";
        Response response = createResponseWithBody(sampleBody);
        
        // Act
        String body = response.getBody();
        
        // Assert
        assertEquals(sampleBody, body);
    }
    
    @Test
    public void testGetBody_NullBody() throws IOException {
        // Arrange
        Response response = createResponseWithBody(null);
        
        // Act
        String body = response.getBody();
        
        // Assert
        assertNull(body);
    }
    
    @Test
    public void testGetStream() throws IOException {
        // Arrange
        Response response = createResponseWithStream();
        
        // Act
        InputStream stream = response.getStream();
        
        // Assert
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() throws IOException {
        // Arrange
        Response response = createResponseWithCode(200);
        
        // Act
        int code = response.getCode();
        
        // Assert
        assertEquals(200, code);
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        // Arrange
        Map<String, String> sampleHeaders = createSampleHeaders();
        Response response = createResponseWithHeaders(sampleHeaders);
        
        // Act
        Map<String, String> headers = response.getHeaders();
        
        // Assert
        assertEquals(sampleHeaders, headers);
    }
    
    @Test
    public void testGetHeader_ValidHeaderName() throws IOException {
        // Arrange
        Map<String, String> sampleHeaders = createSampleHeaders();
        Response response = createResponseWithHeaders(sampleHeaders);
        String validHeaderName = "Content-Type";
        
        // Act
        String headerValue = response.getHeader(validHeaderName);
        
        // Assert
        assertEquals(sampleHeaders.get(validHeaderName), headerValue);
    }
    
    @Test
    public void testGetHeader_InvalidHeaderName() throws IOException {
        // Arrange
        Map<String, String> sampleHeaders = createSampleHeaders();
        Response response = createResponseWithHeaders(sampleHeaders);
        String invalidHeaderName = "Invalid-Header";
        
        // Act
        String headerValue = response.getHeader(invalidHeaderName);
        
        // Assert
        assertNull(headerValue);
    }
    
    private HttpURLConnection createMockConnection(boolean isSuccess, int responseCode) throws IOException {
        if (isSuccess) {
            HttpURLConnection mockConnection = new HttpURLConnection(new URL("http://example.com")) {
                @Override
                public int getResponseCode() throws IOException {
                    return responseCode;
                }
                @Override
                public InputStream getInputStream() throws IOException {
                    // Return a dummy InputStream
                    return new InputStream() {
                        @Override
                        public int read() throws IOException {
                            return -1;
                        }
                    };
                }
                @Override
                public InputStream getErrorStream() {
                    return null;
                }
            };
            return mockConnection;
        } else {
            throw new UnknownHostException();
        }
    }
    
    private Response createResponseWithBody(String body) {
        Response response = new Response(null);
        response.body = body;
        return response;
    }
    
    private Response createResponseWithStream() {
        Response response = new Response(null);
        response.stream = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        return response;
    }
    
    private Response createResponseWithCode(int code) {
        Response response = new Response(null);
        response.code = code;
        return response;
    }
    
    private Response createResponseWithHeaders(Map<String, String> headers) {
        Response response = new Response(null);
        response.headers = headers;
        return response;
    }
    
    private Map<String, String> createSampleHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");
        return headers;
    }
}