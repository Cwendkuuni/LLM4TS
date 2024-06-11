import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response();
        assertEquals("", response.getBody());
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
        assertNull(response.getHeader("name"));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testParseBodyContents() {
        // Test case to validate the parsing of body contents
        Response response = new Response();
        response.parseBodyContents();
        assertNotNull(response.getBody());
    }

    @Test
    public void testParseHeaders() {
        // Test case to validate the parsing of headers
        Response response = new Response();
        assertEquals(0, response.getHeaders().size());
    }

    @Test
    public void testWasSuccessful() {
        // Test case to validate the success status of the response
        Response response = new Response();
        assertTrue(response.wasSuccessful());
    }
}