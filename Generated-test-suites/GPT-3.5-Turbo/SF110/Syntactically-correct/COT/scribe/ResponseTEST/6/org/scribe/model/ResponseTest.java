import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testConstructor() throws IOException {
        // Create a mock HttpURLConnection and test different scenarios
        HttpURLConnection connection = createMockConnection(200); // Successful response
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());

        connection = createMockConnection(404); // UnknownHostException scenario
        response = new Response(connection);
        assertEquals(404, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
        assertEquals("", response.getBody());
    }

    @Test
    public void testParseHeaders() throws IOException {
        // Create a mock HttpURLConnection
        HttpURLConnection connection = createMockConnection(200);

        // Test when response headers are present
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Content-Length", "100");
        Response response = new Response(connection);
        assertEquals(expectedHeaders, response.getHeaders());

        // Test when response headers are not present
        connection = createMockConnection(200);
        response = new Response(connection);
        assertNull(response.getHeaders());
    }

    @Test
    public void testWasSuccessful() {
        // Test successful response
        Response response = new Response(createMockConnection(200));
        assertTrue(response.wasSuccessful());

        // Test unsuccessful response
        response = new Response(createMockConnection(404));
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() throws IOException {
        // Test when body is already parsed
        String expectedBody = "Sample body";
        Response response = new Response(createMockConnection(200));
        response.getBody(); // Parse the body
        assertEquals(expectedBody, response.getBody());

        // Test when body needs to be parsed
        response = new Response(createMockConnection(200));
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        // Test when stream is present
        InputStream expectedStream = createMockInputStream();
        Response response = new Response(createMockConnection(200));
        assertNotNull(response.getStream());
        assertEquals(expectedStream, response.getStream());

        // Test when stream is not present
        response = new Response(createMockConnection(404));
        assertNotNull(response.getStream());
        assertEquals(expectedStream, response.getStream());
    }

    @Test
    public void testGetCode() throws IOException {
        // Test when code is 200
        Response response = new Response(createMockConnection(200));
        assertEquals(200, response.getCode());

        // Test when code is 404
        response = new Response(createMockConnection(404));
        assertEquals(404, response.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        // Test when headers are present
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Content-Length", "100");
        Response response = new Response(createMockConnection(200));
        assertEquals(expectedHeaders, response.getHeaders());

        // Test when headers are not present
        response = new Response(createMockConnection(404));
        assertNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() throws IOException {
        // Test when header is present
        String expectedHeaderValue = "application/json";
        Response response = new Response(createMockConnection(200));
        assertEquals(expectedHeaderValue, response.getHeader("Content-Type"));

        // Test when header is not present
        response = new Response(createMockConnection(200));
        assertNull(response.getHeader("Invalid-Header"));
    }

    // Helper methods for creating mock objects
    private HttpURLConnection createMockConnection(int responseCode) {
        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(connection.getResponseCode()).thenReturn(responseCode);
        Mockito.when(connection.getHeaderFields()).thenReturn(new HashMap<>());
        try {
            Mockito.when(connection.getInputStream()).thenReturn(createMockInputStream());
            Mockito.when(connection.getErrorStream()).thenReturn(createMockInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private InputStream createMockInputStream() {
        return new ByteArrayInputStream("Sample body".getBytes());
    }
}