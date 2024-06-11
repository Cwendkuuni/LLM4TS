
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {
   
    private HttpURLConnection mockConnection;

    @Before
    public void setUp() throws IOException {
        // Create a mock HttpURLConnection for testing
        mockConnection = new HttpURLConnection(null) {
            @Override
            public void connect() throws IOException { }

            @Override
            public int getResponseCode() throws IOException {
                return 200;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
    }
    
    @Test
    public void testGetBody() {
        Response response = new Response(mockConnection);
        
        // Test when body is not null
        response.body = "This is the response body";
        assertEquals("This is the response body", response.getBody());
        
        // Test when body is null
        response.body = null;
        assertNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(mockConnection);
        
        // Test when stream is not null
        response.stream = new ByteArrayInputStream("Test Stream".getBytes());
        assertNotNull(response.getStream());
        
        // Test when stream is null
        response.stream = null;
        assertNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(mockConnection);
        
        // Test when code is 200
        response.code = 200;
        assertEquals(200, response.getCode());
        
        // Test when code is not 200
        response.code = 404;
        assertEquals(404, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(mockConnection);
        
        // Test when headers is not null
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.headers = headers;
        assertEquals(headers, response.getHeaders());
        
        // Test when headers is null
        response.headers = null;
        assertNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(mockConnection);
        
        // Test when header is present
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer token");
        response.headers = headers;
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("Bearer token", response.getHeader("Authorization"));
        
        // Test when header is null
        response.headers = null;
        assertNull(response.getHeader("Content-Type"));
    }
    
    @Test
    public void testConstructor() {
        // Test when connection was successful
        try {
            HttpURLConnection conn = new HttpURLConnection(new URL("http://www.example.com")) {
                @Override
                public void connect() throws IOException { }
                @Override
                public int getResponseCode() throws IOException { return 200; }
                @Override
                public InputStream getInputStream() throws IOException { return null; }
            };
            
            Response response = new Response(conn);
            assertEquals(200, response.getCode());
            assertEquals(null, response.getStream());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test when connection was not successful
        try {
            HttpURLConnection conn = new HttpURLConnection(new URL("http://www.example.com")) {
                @Override
                public void connect() throws IOException { throw new UnknownHostException(); }
                @Override
                public int getResponseCode() throws IOException { return 0; }
                @Override
                public InputStream getErrorStream() { return null; }
            };
            
            Response response = new Response(conn);
            assertEquals(404, response.getCode());
            assertEquals("", response.getBody());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}
