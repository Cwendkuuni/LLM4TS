import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

public class ResponseTest {

    private HttpURLConnection connection;

    @Before
    public void setup() {
        // Simulate a successful connection
        connection = new HttpURLConnection(new URL("http://example.com")) {
            @Override
            public void connect() throws IOException {
                // Do nothing
            }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Sample Body".getBytes());
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public Map<String, String> getHeaderFields() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html");
                headers.put("X-Response-Time", "500ms");
                return headers;
            }
        };
    }

    @Test
    public void testResponse() throws IOException {
        Response response = new Response(connection);
        assertNotNull(response);
    }

    @Test
    public void testGetCode() throws IOException {
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() throws IOException {
        Response response = new Response(connection);
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "text/html");
        expectedHeaders.put("X-Response-Time", "500ms");
        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() throws IOException {
        Response response = new Response(connection);
        assertEquals("text/html", response.getHeader("Content-Type"));
        assertEquals("500ms", response.getHeader("X-Response-Time"));
    }

    @Test
    public void testGetStream() throws IOException {
        Response response = new Response(connection);
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetBody() throws IOException {
        Response response = new Response(connection);
        assertEquals("Sample Body", response.getBody());
    }

    @Test
    public void testGetBody_Null() throws IOException {
        Response response = new Response(connection);
        response = new Response(connection) {
            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
        assertNull(response.getBody());
    }

    @Test
    public void testGetBody_Cached() throws IOException {
        Response response = new Response(connection);
        assertNotNull(response.getBody()); // Caches the body
        assertEquals("Sample Body", response.getBody()); // Retrieves the cached body
    }

    @Test
    public void testUnknownHostException() throws IOException {
        // Simulate an UnknownHostException
        connection = new HttpURLConnection(new URL("http://invalidurl")) {
            @Override
            public void connect() throws IOException {
                throw new UnknownHostException();
            }
        };
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
    }
}