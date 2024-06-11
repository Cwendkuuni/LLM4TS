
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import static org.junit.Assert.*;

public class ResponseTest {
    
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = null; // Mock connection object
        response = new Response(connection);
    }
    
    @Test
    public void testGetBody() {
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(0, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        assertNull(response.getHeader("NonExistingHeader"));
        // Additional test cases specific to headers can be added here
    }
    
}
