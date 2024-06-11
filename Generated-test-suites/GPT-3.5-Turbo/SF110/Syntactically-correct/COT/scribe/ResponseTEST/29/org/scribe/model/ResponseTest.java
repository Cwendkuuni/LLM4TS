import static org.junit.Assert.*;

import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        assertNull(response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(null);
        assertNull(response.getStream());
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
        assertNull(response.getHeader("name"));
    }

    // Additional test cases for edge cases and exception handling

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        assertFalse(response.wasSuccessful());

        Response response2 = new Response(null);
        response2.setCode(200);
        assertTrue(response2.wasSuccessful());
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        assertNotNull(response.parseHeaders(null));
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        assertNull(response.parseBodyContents());
    }

    @Test
    public void testGetHeader_NullValue() {
        Response response = new Response(null);
        response.getHeaders().put("name", null);
        assertNull(response.getHeader("name"));
    }

    @Test
    public void testGetHeader_NotFound() {
        Response response = new Response(null);
        assertNull(response.getHeader("nonexistent"));
    }

    @Test
    public void testGetHeader_ValidValue() {
        Response response = new Response(null);
        response.getHeaders().put("name", "value");
        assertEquals("value", response.getHeader("name"));
    }
}