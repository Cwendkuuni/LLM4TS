import static org.junit.Assert.*;
import org.junit.Test;
import java.net.HttpURLConnection;
import java.io.*;
import java.util.*;

public class ResponseTest {
    
    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        HttpURLConnection connection = createMockConnection(200, "Success");
        
        Response response = new Response(connection);
        
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testConstructor_ErrorConnection() throws IOException {
        HttpURLConnection connection = createMockConnection(500, "Internal Server Error");
        
        Response response = new Response(connection);
        
        assertEquals(500, response.getCode());
        assertEquals("Internal Server Error", response.getBody());
        assertNotNull(response.getStream());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testConstructor_ConnectionError() throws IOException {
        HttpURLConnection connection = createMockConnection(0, null);
        
        Response response = new Response(connection);
        
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = new Response(createMockConnection(200, "Success"));
        
        String body = response.parseBodyContents();
        
        assertEquals("Success", body);
        assertEquals("Success", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        Response response = new Response(createMockConnection(200, "Success"));
        
        Map<String, String> headers = response.parseHeaders(createMockConnection(200, "Success"));
        
        assertEquals(1, headers.size());
        assertEquals("Value", headers.get("Key"));
    }
    
    @Test
    public void testWasSuccessful_Success() {
        Response response = new Response(createMockConnection(200, "Success"));
        
        assertTrue(response.wasSuccessful());
    }
    
    @Test
    public void testWasSuccessful_NotSuccessful() {
        Response response = new Response(createMockConnection(404, "Not Found"));
        
        assertFalse(response.wasSuccessful());
    }
    
    @Test
    public void testGetBody_Initialized() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        
        String body = response.getBody();
        
        assertEquals("Success", body);
    }
    
    @Test
    public void testGetBody_Uninitialized() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        response.getStream().close();
        
        String body = response.getBody();
        
        assertEquals("", body);
    }
    
    @Test
    public void testGetStream() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        
        InputStream stream = response.getStream();
        
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        
        int code = response.getCode();
        
        assertEquals(200, code);
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        
        Map<String, String> headers = response.getHeaders();
        
        assertNotNull(headers);
        assertEquals(1, headers.size());
        assertEquals("Value", headers.get("Key"));
    }
    
    @Test
    public void testGetHeader() throws IOException {
        Response response = new Response(createMockConnection(200, "Success"));
        
        String header = response.getHeader("Key");
        
        assertEquals("Value", header);
    }
    
    // Helper method to create a mock HttpURLConnection
    private HttpURLConnection createMockConnection(int code, String body) throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
            public int getResponseCode() throws IOException {
                return code;
            }
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(body.getBytes());
            }
            public InputStream getErrorStream() {
                return new ByteArrayInputStream();
            }
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Key", Collections.singletonList("Value"));
                return headers;
            }
        };
        return connection;
    }
}