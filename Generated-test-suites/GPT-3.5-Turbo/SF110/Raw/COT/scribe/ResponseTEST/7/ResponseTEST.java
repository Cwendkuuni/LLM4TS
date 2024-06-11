
import static org.junit.Assert.*;
import org.junit.*;
import org.scribe.model.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {
    
    private HttpURLConnection connection;
    private Response response;
    
    @Before
    public void setUp() throws IOException {
        URL url = new URL("http://example.com/");
        connection = (HttpURLConnection) url.openConnection();
        response = new Response(connection);
    }
    
    @After
    public void tearDown() throws IOException {
        connection.disconnect();
        response = null;
    }
    
    @Test
    public void testGetBody() {
        String body = response.getBody();
        assertNotNull(body);
    }
    
    @Test
    public void testGetStream() {
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() {
        int code = response.getCode();
        assertTrue(code >= 0);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
    }
    
    @Test
    public void testGetHeader() {
        String header = response.getHeader("Content-Type");
        assertNull(header);
    }
    
    @Test
    public void testConstructor_unknownHostException() throws IOException {
        URL url = new URL("http://unknownhost/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            response = new Response(connection);
            assertEquals(404, response.getCode());
            assertEquals("", response.getBody());
        } finally {
            connection.disconnect();
            response = null;
        }
    }
    
}
