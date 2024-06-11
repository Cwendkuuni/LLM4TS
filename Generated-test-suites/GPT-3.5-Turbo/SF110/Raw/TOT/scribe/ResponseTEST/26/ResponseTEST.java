import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
            public int getResponseCode() {
                return 200;
            }
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Example Body".getBytes());
            }
            public Map<String, List<String>> getHeaderFields() {
                HashMap<String, List<String>> headers = new HashMap<>();
                headers.put("Content-Type", Arrays.asList("text/plain"));
                return headers;
            }
        };
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        assertEquals("Example Body", response.getBody());
    }

    @Test
    public void testGetBodyWhenBodyIsNull() {
        response.body = null;
        assertEquals("Example Body", response.getBody());
    }

    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/plain");
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        assertEquals("text/plain", response.getHeader("Content-Type"));
    }

    @Test
    public void testParseBodyContents() {
        assertEquals("Example Body", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://www.example.com")) {
            public Map<String, List<String>> getHeaderFields() {
                HashMap<String, List<String>> headers = new HashMap<>();
                headers.put("Content-Type", Arrays.asList("text/plain"));
                headers.put("Expires", Arrays.asList("Wed, 21 Oct 2015 07:28:00 GMT"));
                return headers;
            }
        };
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/plain");
        expectedHeaders.put("Expires", "Wed, 21 Oct 2015 07:28:00 GMT");
        assertEquals(expectedHeaders, response.parseHeaders(connection));
    }

    @Test
    public void testWasSuccessfulWhenCodeIs200() {
        response.code = 200;
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWhenCodeIs399() {
        response.code = 399;
        assertTrue(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWhenCodeIs400() {
        response.code = 400;
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWhenCodeIs199() {
        response.code = 199;
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testWasSuccessfulWhenCodeIs300() {
        response.code = 300;
        assertFalse(response.wasSuccessful());
    }
}