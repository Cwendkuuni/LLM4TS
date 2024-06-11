import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody_typical() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public int getResponseCode() throws IOException { return 200; }
            public InputStream getInputStream() throws IOException { return new ByteArrayInputStream("response body".getBytes()); }
        };
        Response response = new Response(connection);
        assertEquals("response body", response.getBody());
    }

    @Test
    public void testGetBody_nullBody() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public int getResponseCode() throws IOException { return 200; }
            public InputStream getInputStream() throws IOException { return null; }
        };
        Response response = new Response(connection);
        assertNull(response.getBody());
    }

    @Test
    public void testGetBody_IOException() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public int getResponseCode() throws IOException { return 200; }
            public InputStream getInputStream() throws IOException { throw new IOException("IO error"); }
        };
        Response response = new Response(connection);
        assertNull(response.getBody());
    }

    @Test
    public void testGetCode_typical() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public int getResponseCode() throws IOException { return 200; }
        };
        Response response = new Response(connection);
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetCode_ioException() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public int getResponseCode() throws IOException { throw new IOException("IO error"); }
        };
        Response response = new Response(connection);
        assertEquals(404, response.getCode());
    }

    @Test
    public void testGetHeaders_typical() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public Map<String, List<String>> getHeaderFields() { 
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Content-Type", Collections.singletonList("application/json"));
                return headers;
            }
        };
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }

    @Test
    public void testGetHeaders_emptyHeaders() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public Map<String, List<String>> getHeaderFields() { 
                Map<String, List<String>> headers = new HashMap<>();
                return headers;
            }
        };
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertTrue(headers.isEmpty());
    }

    @Test
    public void testGetHeader_typical() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public Map<String, List<String>> getHeaderFields() { 
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Content-Type", Collections.singletonList("application/json"));
                headers.put("Content-Length", Collections.singletonList("100"));
                return headers;
            }
        };
        Response response = new Response(connection);
        assertEquals("application/json", response.getHeader("Content-Type"));
    }

    @Test
    public void testGetHeader_missingHeader() throws Exception {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://test.com")) {
            public void disconnect(){}
            public boolean usingProxy() { return false; }
            public void connect() throws IOException { }
            public Map<String, List<String>> getHeaderFields() { 
                Map<String, List<String>> headers = new HashMap<>();
                headers.put("Content-Type", Collections.singletonList("application/json"));
                headers.put("Content-Length", Collections.singletonList("100"));
                return headers;
            }
        };
        Response response = new Response(connection);
        assertNull(response.getHeader("Authorization"));
    }

}