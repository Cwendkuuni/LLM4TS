import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class ResponseTest {

    // Test for the constructor
    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
            public void connect() {}
            public void disconnect() {}
            public boolean usingProxy() { return false; }
        };
        Response response = new Response(connection);
        assertNotNull(response);
    }

    // Test for the parseBodyContents() method
    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        response.getStream = new ByteArrayInputStream("Test Body".getBytes());
        String bodyContents = response.parseBodyContents();
        assertEquals("Test Body", bodyContents);
    }

    // Test for the parseHeaders() method
    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
            public void connect() {}
            public void disconnect() {}
            public boolean usingProxy() { return false; }
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<>();
                List<String> values = new ArrayList<>();
                values.add("Value1");
                values.add("Value2");
                headers.put("Header1", values);
                return headers;
            }
        };
        Map<String, String> parsedHeaders = response.parseHeaders(connection);
        assertEquals(2, parsedHeaders.size());
        assertEquals("Value1", parsedHeaders.get("Header1"));
        assertEquals("Value2", parsedHeaders.get("Header2"));
    }

    // Test for the wasSuccessful() method
    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.getCode = 200;
        assertTrue(response.wasSuccessful());
        response.getCode = 199;
        assertFalse(response.wasSuccessful());
        response.getCode = 400;
        assertFalse(response.wasSuccessful());
    }

    // Test for the getBody() method
    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.body = "Test Body";
        String body = response.getBody();
        assertEquals("Test Body", body);
    }

    // Test for the getStream() method
    @Test
    public void testGetStream() {
        // Testing with a dummy input stream
        Response response = new Response(null);
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }

    // Test for the getCode() method
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.code = 200;
        int code = response.getCode();
        assertEquals(200, code);
    }

    // Test for the getHeaders() method
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        response.headers = headers;
        Map<String, String> retrievedHeaders = response.getHeaders();
        assertEquals(2, retrievedHeaders.size());
        assertEquals("Value1", retrievedHeaders.get("Header1"));
        assertEquals("Value2", retrievedHeaders.get("Header2"));
    }

    // Test for the getHeader() method
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Header1", "Value1");
        headers.put("Header2", "Value2");
        response.headers = headers;
        String header = response.getHeader("Header1");
        assertEquals("Value1", header);
    }

}