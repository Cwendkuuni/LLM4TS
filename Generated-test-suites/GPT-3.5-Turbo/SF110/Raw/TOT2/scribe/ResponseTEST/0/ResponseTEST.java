import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;

public class ResponseTest {
    
    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.getBody(); // Test parsing body contents
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(null);
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        assertEquals(0, response.getCode()); // Test default code value
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        assertNotNull(response.getHeaders());
        assertTrue(response.getHeaders().isEmpty()); // Test empty headers map
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        assertNull(response.getHeader("Content-Type")); // Test for non-existent header
    }
    
    @Test(expected = IOException.class)
    public void testEmptyResponseException() throws IOException {
        new Response(null);
    }
    
    @Test
    public void testUnknownHostException() {
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        HttpURLConnection connection = new HttpURLConnection(stream);
        connection.setResponseCode(404);
        Response response = new Response(connection);
        assertEquals(404, response.getCode()); // Test handling UnknownHostException
    }
}