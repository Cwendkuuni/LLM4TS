import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(createMockConnection());
        assertEquals("Expected empty body", "", response.getBody());
    }

    @Test
    public void testGetStream() throws IOException {
        Response response = new Response(createMockConnection());
        assertNotNull("Expected non-null stream", response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(createMockConnection());
        assertEquals("Expected code 200", 200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(createMockConnection());
        assertNotNull("Expected non-null headers", response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(createMockConnection());
        String expectedHeader = "header1";
        String expectedValue = "value1";
        response.getHeaders().put(expectedHeader, expectedValue);
        assertEquals("Expected header value", expectedValue, response.getHeader(expectedHeader));
    }

    private HttpURLConnection createMockConnection() {
        return new HttpURLConnectionMock();
    }

    private class HttpURLConnectionMock extends HttpURLConnection {

        public HttpURLConnectionMock() {
            super(null);
        }

        public int getResponseCode() {
            return 200;
        }

        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> headers = new HashMap<String, List<String>>();
            headers.put("header1", Arrays.asList("value1"));
            headers.put("header2", Arrays.asList("value2"));
            return headers;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream("response body".getBytes());
        }

        public InputStream getErrorStream() {
            return null;
        }

        public void connect() throws IOException {

        }
    }
}