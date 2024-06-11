import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class ResponseTest {

    private HttpURLConnection mockSuccessConnection;
    private HttpURLConnection mockErrorConnection;

    @Before
    public void setUp() throws IOException {
        mockSuccessConnection = createMockConnection(200, "Success Body", createMockHeaders());
        mockErrorConnection = createMockConnection(404, "Error Body", createMockHeaders());
    }

    @Test
    public void testGetBody() {
        Response response = new Response(mockSuccessConnection);
        String expectedBody = "Success Body";
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(mockSuccessConnection);
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(mockSuccessConnection);
        int expectedCode = 200;
        assertEquals(expectedCode, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(mockSuccessConnection);
        Map<String, String> expectedHeaders = createMockHeaders();
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(mockSuccessConnection);
        String expectedValue = "Value";
        assertEquals(expectedValue, response.getHeader("Key"));
    }

    @Test
    public void testUnknownHostException() {
        Response response = new Response(mockErrorConnection);
        int expectedCode = 404;
        assertEquals(expectedCode, response.getCode());
        assertEquals("", response.getBody());
    }

    private HttpURLConnection createMockConnection(int code, String body, Map<String, List<String>> headers) {
        HttpURLConnection mockConnection = org.mockito.Mockito.mock(HttpURLConnection.class);
        try {
            when(mockConnection.getResponseCode()).thenReturn(code);
            when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
            when(mockConnection.getErrorStream()).thenReturn(new ByteArrayInputStream(body.getBytes()));
            when(mockConnection.getHeaderFields()).thenReturn(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mockConnection;
    }

    private Map<String, String> createMockHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Key", "Value");
        headers.put("Content-Type", "application/json");
        return headers;
    }
}