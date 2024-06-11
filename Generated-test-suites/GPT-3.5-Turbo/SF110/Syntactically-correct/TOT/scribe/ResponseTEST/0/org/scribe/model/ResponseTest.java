import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testConstructor_NormalConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertEquals("OK", response.getHeader("Status"));
        assertNotNull(response.getStream());
        assertTrue(response.getHeaders().containsKey("Content-Type"));
    }

    @Test
    public void testConstructor_ConnectionError() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://nonexistingdomain.com").openConnection();
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
    }

    @Test
    public void testGetBody_NotParsed() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        assertNull(response.getBody());
    }

    @Test
    public void testGetBody_Parsed() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        String expectedBody = "<html><body>Hello World!</body></html>";
        when(response.getStream()).thenReturn(new ByteArrayInputStream(expectedBody.getBytes()));

        assertEquals(expectedBody, response.getBody());
        assertEquals(expectedBody, response.getBody()); // Ensure it doesn't parse again
    }

    @Test
    public void testGetStream() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertFalse(headers.isEmpty());
    }

    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
        Response response = new Response(connection);

        String contentType = response.getHeader("Content-Type");
        assertNotNull(contentType);
        assertEquals("text/html", contentType);
    }

}