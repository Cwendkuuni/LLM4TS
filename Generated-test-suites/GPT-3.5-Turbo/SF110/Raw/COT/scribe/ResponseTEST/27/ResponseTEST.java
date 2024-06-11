
import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response();
        response.body = "Hello World";
        
        assertEquals("Hello World", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response();
        response.stream = new ByteArrayInputStream("test".getBytes());
        
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response();
        response.code = 200;
        
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response();
        response.headers = new HashMap<>();
        response.headers.put("Content-Type", "application/json");
        
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    @Test
    public void testGetHeader() {
        Response response = new Response();
        response.headers = new HashMap<>();
        response.headers.put("Content-Type", "application/json");
        
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

}
