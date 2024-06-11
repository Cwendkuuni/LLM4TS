import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class ResponseTest {
    
    @Test
    public void testGetBody() {
        Response response = new Response(null);
        
        // Test when body is already parsed
        response.body = "Test Body";
        assertEquals("Test Body", response.getBody());
        
        // Test when body needs to be parsed
        response.body = null;
        response.stream = new ByteArrayInputStream("Test Body".getBytes());
        assertEquals("Test Body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(null);
        InputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        response.stream = inputStream;
        
        assertEquals(inputStream, response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.code = 200;
        
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "text/html");
        response.headers = headers;
        
        assertEquals(headers, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "text/html");
        response.headers = headers;
        
        assertEquals("text/html", response.getHeader("Content-Type"));
        assertNull(response.getHeader("Invalid-Header"));
    }
    
    @Test
    public void testConstructor() {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public void connect() throws IOException {}
            
            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }
            
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("Test Stream".getBytes());
            }
        };
        
        try {
            Response response = new Response(connection);
            
            assertEquals(200, response.getCode());
            assertNotNull(response.getStream());
            assertTrue(response.getBody().contains("Test Stream"));
            assertNotNull(response.getHeaders());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        InputStream inputStream = new ByteArrayInputStream("Test Stream".getBytes());
        response.stream = inputStream;
        
        assertEquals("Test Stream", response.parseBodyContents());
        assertEquals("Test Stream", response.body);
    }
    
    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = new HttpURLConnection(null) {
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headers = new HashMap<String, List<String>>();
                List<String> values = new ArrayList<String>();
                values.add("text/html");
                headers.put("Content-Type", values);
                return headers;
            }
        };
        
        Response response = new Response(null);
        Map<String, String> headers = response.parseHeaders(connection);
        
        assertEquals("text/html", headers.get("Content-Type"));
    }
    
    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.code = 200;
        assertTrue(response.wasSuccessful());
        
        response.code = 400;
        assertFalse(response.wasSuccessful());
        
        response.code = 500;
        assertFalse(response.wasSuccessful());
    }
}