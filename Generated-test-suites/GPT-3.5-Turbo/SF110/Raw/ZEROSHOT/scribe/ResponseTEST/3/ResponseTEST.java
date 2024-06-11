import org.junit.*;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response();
        String body = response.getBody();
        assertEquals("", body);
    }

    @Test
    public void testGetStream() {
        Response response = new Response();
        InputStream stream = response.getStream();
        assertNull(stream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response();
        int code = response.getCode();
        assertEquals(0, code);
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response();
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(0, headers.size());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response();
        String header = response.getHeader("Name");
        assertNull(header);
    }
}