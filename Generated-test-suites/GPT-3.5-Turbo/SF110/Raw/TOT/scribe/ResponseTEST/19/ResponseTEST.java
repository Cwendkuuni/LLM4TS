import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class ResponseTest {

    @Test
    public void testGetBody_WhenBodyNotNull_ShouldReturnBody() {
        Response response = createResponse(200, "Sample body", null, null);

        assertEquals("Sample body", response.getBody());
    }

    @Test
    public void testGetBody_WhenBodyNull_ShouldReturnParsedBodyContents() {
        Response response = createResponse(200, null, null, "Sample parsed body");

        assertEquals("Sample parsed body", response.getBody());
    }

    @Test
    public void testGetStream_ShouldReturnStream() throws IOException {
        InputStream stream = new ByteArrayInputStream("Sample stream".getBytes());
        Response response = createResponse(200, null, stream, null);

        assertEquals(stream, response.getStream());
    }

    @Test
    public void testGetCode_ShouldReturnCode() {
        Response response = createResponse(200, null, null, null);

        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders_ShouldReturnHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");

        Response response = createResponse(200, null, null, headers);

        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testGetHeader_WhenHeaderExists_ShouldReturnHeaderValue() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");

        Response response = createResponse(200, null, null, headers);

        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testGetHeader_WhenHeaderDoesNotExist_ShouldReturnNull() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");

        Response response = createResponse(200, null, null, headers);

        assertNull(response.getHeader("Authorization"));
    }

    private Response createResponse(int code, String body, InputStream stream, Map<String, String> headers) {
        try {
            HttpURLConnection connection = createMockConnection(code, body, stream, headers);
            return new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection createMockConnection(int code, String body, InputStream stream, Map<String, String> headers) {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        try {
            when(connection.getResponseCode()).thenReturn(code);
            when(connection.getInputStream()).thenReturn(stream);
            when(connection.getErrorStream()).thenReturn(null);
            when(connection.getHeaderFields()).thenReturn(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}