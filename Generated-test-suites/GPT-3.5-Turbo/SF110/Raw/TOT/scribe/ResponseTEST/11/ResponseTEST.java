import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;

public class ResponseTest {
    
    private Response response;
    
    @Before
    public void setUp() throws Exception {
        // Create a mock HttpURLConnection
        HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
        // Set up the necessary values for the Response object
        Mockito.when(mockedConnection.connect()).thenReturn(null);
        Mockito.when(mockedConnection.getResponseCode()).thenReturn(200);
        Mockito.when(mockedConnection.getInputStream()).thenReturn(null);
        Mockito.when(mockedConnection.getErrorStream()).thenReturn(null);
        response = new Response(mockedConnection);
    }
    
    @Test
    public void testGetBody() {
        // Test typical use case
        String body = "Hello, world!";
        response.body = body;
        assertEquals(body, response.getBody());
        
        // Test alternative case when body is null
        response.body = null;
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        // Test typical use case
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        // Test typical use case
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        // Test typical use case
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        // Test typical use case
        String key = "Content-Type";
        String value = "application/json";
        response.headers.put(key, value);
        assertEquals(value, response.getHeader(key));
        
        // Test case where header does not exist
        assertNull(response.getHeader("Invalid-Key"));
    }
}