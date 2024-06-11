import static org.junit.Assert.*;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        String body = "Test body";
        response.getBody();
        assertEquals("Test body", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(null);
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        assertEquals(0, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        response.getHeaders().put("Test", "Header");
        assertEquals("Header", response.getHeader("Test"));
    }

}