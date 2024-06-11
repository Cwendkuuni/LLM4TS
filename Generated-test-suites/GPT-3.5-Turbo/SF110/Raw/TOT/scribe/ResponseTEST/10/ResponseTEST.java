import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testConstructor_SuccessfulConnection() throws IOException {
        HttpURLConnection connection = createMockSuccessfulConnection();
        Response response = new Response(connection);

        assertEquals(200, response.getCode());
        assertEquals(connection.getInputStream(), response.getStream());
        assertEquals(null, response.getBody());
        assertEquals(connection.getHeaderFields(), response.getHeaders());
    }

    @Test
    public void testConstructor_UnsuccessfulConnection() throws IOException {
        HttpURLConnection connection = createMockUnsuccessfulConnection();
        Response response = new Response(connection);

        assertEquals(404, response.getCode());
        assertEquals(connection.getErrorStream(), response.getStream());
        assertEquals("", response.getBody());
        assertEquals(connection.getHeaderFields(), response.getHeaders());
    }

    // Test for parseBodyContents() method
    @Test
    public void testParseBodyContents() {
        Response response = createMockResponseWithBody();
        String expectedBody = "Test Body";
        assertEquals(expectedBody, response.parseBodyContents());
        assertEquals(expectedBody, response.getBody());
    }

    // Test for parseHeaders() method
    @Test
    public void testParseHeaders() {
        Response response = createMockResponseWithHeaders();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        assertEquals(expectedHeaders, response.getHeaders());
    }

    // Test for wasSuccessful() method
    @Test
    public void testWasSuccessful_True() {
        Response response = createMockResponseWithCode(200);
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessful_False() {
        Response response = createMockResponseWithCode(400);
        assertFalse(response.wasSuccessful());
    }

    // Test for getBody() method
    @Test
    public void testGetBody_WithExistingBody() {
        Response response = createMockResponseWithBody();
        String expectedBody = "Test Body";
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetBody_WithoutExistingBody() {
        Response response = createMockResponse();
        String expectedBody = "Test Body";
        assertEquals(expectedBody, response.getBody());
    }

    // Test for getStream() method
    @Test
    public void testGetStream() {
        Response response = createMockResponse();
        InputStream expectedStream = createMockInputStream();
        assertEquals(expectedStream, response.getStream());
    }

    // Test for getCode() method
    @Test
    public void testGetCode() {
        Response response = createMockResponseWithCode(200);
        assertEquals(200, response.getCode());
    }

    // Test for getHeaders() method
    @Test
    public void testGetHeaders() {
        Response response = createMockResponseWithHeaders();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        assertEquals(expectedHeaders, response.getHeaders());
    }

    // Test for getHeader() method
    @Test
    public void testGetHeader() {
        Response response = createMockResponseWithHeaders();
        String expectedValue = "application/json";
        assertEquals(expectedValue, response.getHeader("Content-Type"));
    }

    // Utility methods to create mock objects for testing

    private HttpURLConnection createMockSuccessfulConnection() {
        HttpURLConnection connection = new HttpURLConnectionStub();
        connection.setResponseCode(200);
        connection.addHeader("Content-Type", "application/json");
        connection.addHeader("Cache-Control", "no-cache");
        connection.setInputStream(createMockInputStream());

        return connection;
    }

    private HttpURLConnection createMockUnsuccessfulConnection() {
        HttpURLConnection connection = new HttpURLConnectionStub();
        connection.setResponseCode(404);
        connection.setErrorStream(createMockInputStream());
        
        return connection;
    }

    private Response createMockResponse() {
        HttpURLConnection connection = new HttpURLConnectionStub();
        return new Response(connection);
    }

    private Response createMockResponseWithCode(int code) {
        HttpURLConnection connection = new HttpURLConnectionStub();
        connection.setResponseCode(code);
        return new Response(connection);
    }

    private Response createMockResponseWithBody() {
        HttpURLConnection connection = new HttpURLConnectionStub();
        connection.setResponseCode(200);
        connection.setInputStream(createMockInputStream());
        return new Response(connection);
    }

    private Response createMockResponseWithHeaders() {
        HttpURLConnection connection = new HttpURLConnectionStub();
        connection.setResponseCode(200);
        connection.addHeader("Content-Type", "application/json");
        connection.addHeader("Cache-Control", "no-cache");
        return new Response(connection);
    }

    private InputStream createMockInputStream() {
        // Create a mock InputStream for testing purposes
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
    }

    // Stub class to mock HttpURLConnection
    private class HttpURLConnectionStub extends HttpURLConnection {
        HttpURLConnectionStub() {
            super(null);
        }
        
        @Override
        public void disconnect() {
        }
        
        @Override
        public boolean usingProxy() {
            return false;
        }
        
        @Override
        public void connect() throws IOException {
        }
    }
}