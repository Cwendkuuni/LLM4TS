import org.junit.*;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Test case 1: When body is null
        Response response1 = new Response(null);
        assertNull(response1.getBody());

        // Test case 2: When body is not null
        Response response2 = new Response(null);
        response2.setBody("Test body");
        assertEquals("Test body", response2.getBody());
    }

    @Test
    public void testGetStream() {
        // Test case 1: When stream is null
        Response response1 = new Response(null);
        assertNull(response1.getStream());

        // Test case 2: When stream is not null
        Response response2 = new Response(null);
        response2.setStream(new InputStream());
        assertNotNull(response2.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.setCode(200);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testConstructor() {
        // Test case 1: UnknownHostException
        Response response1 = new Response(null);
        response1.setUnknownHostException(true);
        assertEquals(404, response1.getCode());
        assertEquals("", response1.getBody());

        // Test case 2: Successful connection
        Response response2 = new Response(null);
        response2.setCode(200);
        response2.setStream(new InputStream());
        assertEquals(200, response2.getCode());
        assertNotNull(response2.getStream());
    }

    private class InputStream extends java.io.InputStream {
        // Implement necessary methods
    }
}