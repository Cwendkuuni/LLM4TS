import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        String body = response.parseBodyContents();
        assertEquals("", body);
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = response.parseHeaders(null);
        assertTrue(headers.isEmpty());
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.getCode();
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        String body = response.getBody();
        assertEquals("", body);
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
        Map<String, String> headers = response.getHeaders();
        assertTrue(headers.isEmpty());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        assertNull(response.getHeader("key"));
    }
}