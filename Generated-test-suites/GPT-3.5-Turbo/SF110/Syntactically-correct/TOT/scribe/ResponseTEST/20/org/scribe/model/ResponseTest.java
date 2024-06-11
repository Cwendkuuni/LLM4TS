import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UnknownHostException;

public class ResponseTest {
    
    // Test case for Response constructor with successful connection
    @Test
    public void testResponseConstructorSuccessful() throws IOException {
        HttpURLConnection connection = createMockSuccessfulConnection();
        Response response = new Response(connection);
        
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertNull(response.getBody()); // Body should not be parsed during instantiation
        assertNull(response.getHeader("SomeHeader"));
    }
    
    // Test case for Response constructor with unsuccessful connection
    @Test
    public void testResponseConstructorUnsuccessful() throws IOException {
        HttpURLConnection connection = createMockUnsuccessfulConnection();
        Response response = new Response(connection);
        
        assertEquals(404, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());
        assertNull(response.getHeader("SomeHeader"));
    }
    
    // Test case for parseBodyContents method
    @Test
    public void testParseBodyContents() throws IOException {
        HttpURLConnection connection = createMockSuccessfulConnection();
        Response response = new Response(connection);
        
        assertEquals("Response body", response.parseBodyContents());
        assertEquals("Response body", response.getBody());
    }
    
    // Test case for parseHeaders method
    @Test
    public void testParseHeaders() throws IOException {
        HttpURLConnection connection = createMockSuccessfulConnection();
        Response response = new Response(connection);
        
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Server", "Apache");
        
        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("Apache", response.getHeader("Server"));
        assertNull(response.getHeader("SomeHeader"));
    }
    
    // Test case for wasSuccessful method
    @Test
    public void testWasSuccessful() throws IOException {
        HttpURLConnection connection = createMockSuccessfulConnection();
        Response response = new Response(connection);
        
        assertTrue(response.wasSuccessful());
    }
    
    // Helper method to create a mock HttpURLConnection with a successful response
    private HttpURLConnection createMockSuccessfulConnection() throws IOException {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Map<String, java.util.List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        headers.put("Server", Arrays.asList("Apache"));
        Mockito.when(connection.getHeaderFields()).thenReturn(headers);
        InputStream stream = new ByteArrayInputStream("Response body".getBytes());
        Mockito.when(connection.getInputStream()).thenReturn(stream);
        
        return connection;
    }
    
    // Helper method to create a mock HttpURLConnection with an unsuccessful response
    private HttpURLConnection createMockUnsuccessfulConnection() throws IOException {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        
        Mockito.when(connection.getResponseCode()).thenReturn(404);
        Mockito.when(connection.getHeaderFields()).thenReturn(new HashMap<>());
        InputStream stream = new ByteArrayInputStream("".getBytes());
        Mockito.when(connection.getErrorStream()).thenReturn(stream);
        
        return connection;
    }
}