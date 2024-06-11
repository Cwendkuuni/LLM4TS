import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class ResponseTest {
    private HttpURLConnection connection;
    private Response response;

    @Before
    public void setUp() throws IOException {
        connection = mock(HttpURLConnection.class);
        response = new Response(connection);
    }

    @Test
    public void testResponseConstructor_SuccessfulConnection() throws IOException {
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, String>());
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("Test Body".getBytes()));

        response = new Response(connection);

        assertEquals(200, response.getCode());
        assertEquals("Test Body", response.getBody());
        assertEquals(new HashMap<String, String>(), response.getHeaders());
        assertNull(response.getStream());
    }

    @Test
    public void testResponseConstructor_UnsuccessfulConnection() throws IOException {
        when(connection.getResponseCode()).thenReturn(404);

        response = new Response(connection);

        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getHeaders());
        assertNull(response.getStream());
    }

    @Test
    public void testParseBodyContents() {
        String body = response.parseBodyContents();

        assertEquals("", body);
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        when(connection.getHeaderFields()).thenReturn(headers);

        Map<String, String> parsedHeaders = response.parseHeaders(connection);

        assertEquals(headers, parsedHeaders);
    }

    @Test
    public void testWasSuccessful_SuccessfulCode() {
        response = new Response(connection);
        response.getCode();

        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessful_UnsuccessfulCode() throws IOException {
        when(connection.getResponseCode()).thenReturn(400);

        response = new Response(connection);
        response.getCode();

        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody_BodyNotNull() {
        response = new Response(connection);
        response.parseBodyContents();

        String body = response.getBody();

        assertEquals("", body);
    }

    @Test
    public void testGetBody_BodyNull() {
        response = new Response(connection);

        String body = response.getBody();

        assertEquals("", body);
    }

    @Test
    public void testGetStream() {
        InputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        when(connection.getInputStream()).thenReturn(inputStream);

        response = new Response(connection);

        assertNotNull(response.getStream());
        assertEquals(inputStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        response = new Response(connection);

        assertEquals(connection.getResponseCode(), response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        when(connection.getHeaderFields()).thenReturn(headers);

        response = new Response(connection);

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        when(connection.getHeaderFields()).thenReturn(headers);

        response = new Response(connection);

        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}