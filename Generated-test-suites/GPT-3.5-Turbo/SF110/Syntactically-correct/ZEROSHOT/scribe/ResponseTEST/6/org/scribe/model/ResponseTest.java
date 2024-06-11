import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = null;
        try {
            response = new Response(createDummyConnection());
            assertEquals("", response.getBody());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetStream() {
        Response response = null;
        try {
            response = new Response(createDummyConnection());
            assertNotNull(response.getStream());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetCode() {
        Response response = null;
        try {
            response = new Response(createDummyConnection());
            assertEquals(200, response.getCode());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetHeaders() {
        Response response = null;
        try {
            response = new Response(createDummyConnection());
            assertNotNull(response.getHeaders());
            assertEquals(2, response.getHeaders().size());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetHeader() {
        Response response = null;
        try {
            response = new Response(createDummyConnection());
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("text/html", response.getHeader("Content-Type"));
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    private HttpURLConnection createDummyConnection() {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public void disconnect() {}

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public String getHeaderField(String name) {
                if (name.equals("Content-Type")) return "text/html";
                return null;
            }

            @Override
            public int getResponseCode() {
                return 200;
            }

            @Override
            public Map<String, java.util.List<String>> getHeaderFields() {
                Map<String, java.util.List<String>> headers = new HashMap<>();
                headers.put("Content-Type", java.util.Collections.singletonList("text/html"));
                headers.put("Content-Length", java.util.Collections.singletonList("100"));
                return headers;
            }
        };
        return connection;
    }
}