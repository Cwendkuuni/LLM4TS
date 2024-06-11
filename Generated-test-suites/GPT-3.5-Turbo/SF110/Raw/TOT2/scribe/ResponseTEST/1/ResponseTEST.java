import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.model.Response;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response();
        String body = response.getBody();
        assertNotNull(body);
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response();
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response();
        assertEquals(response.getCode(), 0);
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response();
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response();
        assertNull(response.getHeader("NonExistentHeader"));
    }

}