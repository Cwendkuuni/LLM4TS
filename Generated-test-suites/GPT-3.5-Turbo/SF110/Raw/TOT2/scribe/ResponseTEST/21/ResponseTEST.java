import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com"));
        response = new Response(connection);
    }
    
    @Test
    public void testGetBody() {
        assertEquals("Testing getBody method", response.getBody(), "");
    }
    
    @Test
    public void testGetStream() {
        assertNotNull("Testing getStream method", response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals("Testing getCode method", response.getCode(), 200);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> testHeaders = new HashMap<>();
        testHeaders.put("Content-Type", "application/json");
        assertEquals("Testing getHeaders method", response.getHeaders(), testHeaders);
    }
    
    @Test
    public void testGetHeader() {
        assertEquals("Testing getHeader method", response.getHeader("Content-Type"), "application/json");
    }
    
    @Test
    public void testParseBodyContents() {
        assertEquals("Testing parseBodyContents method", response.parseBodyContents(), "");
    }
    
    @Test
    public void testWasSuccessful() {
        assertTrue("Testing wasSuccessful method", response.wasSuccessful());
    }
}