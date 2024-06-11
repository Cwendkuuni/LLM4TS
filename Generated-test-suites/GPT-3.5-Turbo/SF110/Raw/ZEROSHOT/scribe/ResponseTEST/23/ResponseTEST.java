import static org.junit.Assert.*;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Test case 1: Body is not null
        Response response1 = new Response(null);
        response1.getBody();
        assertNotNull(response1.getBody());

        // Test case 2: Body is null
        Response response2 = new Response(null);
        response2.getBody();
        assertNull(response2.getBody());
    }

    @Test
    public void testGetStream() {
        // Test case 1: Stream is not null
        Response response1 = new Response(null);
        assertNotNull(response1.getStream());

        // Test case 2: Stream is null
        Response response2 = new Response(null);
        assertNotNull(response2.getStream());
    }

    @Test
    public void testGetCode() {
        // Test case 1: Code is 200
        Response response1 = new Response(null);
        response1.getCode();
        assertEquals(200, response1.getCode());

        // Test case 2: Code is 404
        Response response2 = new Response(null);
        response2.getCode();
        assertEquals(404, response2.getCode());
    }

    @Test
    public void testGetHeaders() {
        // Test case 1: Headers is not null
        Response response1 = new Response(null);
        assertNotNull(response1.getHeaders());

        // Test case 2: Headers is null
        Response response2 = new Response(null);
        assertNotNull(response2.getHeaders());
    }

    @Test
    public void testGetHeader() {
        // Test case 1: Header value is not null
        Response response1 = new Response(null);
        response1.getHeaders().put("testHeader", "testValue");
        assertNotNull(response1.getHeader("testHeader"));

        // Test case 2: Header value is null
        Response response2 = new Response(null);
        assertNull(response2.getHeader("testHeader"));
    }
}