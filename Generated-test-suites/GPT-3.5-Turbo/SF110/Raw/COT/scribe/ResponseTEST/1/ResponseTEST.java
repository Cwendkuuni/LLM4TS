import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {
    
    private Response response;
    
    @Before
    public void setup() {
        response = new Response();
    }
    
    @Test
    public void testGetBody() {
        // test with non-null body
        response.body = "test body";
        assertEquals("test body", response.getBody());
        
        // test with null body
        response.body = null;
        response.stream = new ByteArrayInputStream("test body".getBytes());
        assertEquals("test body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        // test with non-null stream
        response.stream = new ByteArrayInputStream("test stream".getBytes());
        assertSame(response.stream, response.getStream());
        
        // test with null stream
        response.stream = null;
        assertNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        response.code = 200;
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        // test with non-null headers
        response.headers = new HashMap<>();
        response.headers.put("Content-Type", "application/json");
        assertEquals(response.headers, response.getHeaders());
        
        // test with null headers
        response.headers = null;
        assertNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        response.headers = new HashMap<>();
        response.headers.put("Content-Type", "application/json");
        response.headers.put("Cache-Control", "no-cache");
        
        // test with existing header name
        assertEquals("application/json", response.getHeader("Content-Type"));
        
        // test with non-existing header name
        assertNull(response.getHeader("Authorization"));
    }
}