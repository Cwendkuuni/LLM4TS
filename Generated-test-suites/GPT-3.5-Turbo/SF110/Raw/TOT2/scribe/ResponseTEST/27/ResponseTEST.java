import static org.junit.Assert.*;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response();
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response();
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response();
        assertEquals(0, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response();
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response();
        assertNull(response.getHeader("test"));
    }

    @Test
    public void testGetBodyNull() {
        Response response = new Response();
        response.setBody(null);
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetHeaderEmpty() {
        Response response = new Response();
        response.setHeaders(new HashMap<String, String>());
        assertNull(response.getHeader("test"));
    }
}