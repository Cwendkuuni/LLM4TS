import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    private Response response;
    
    @Before
    public void setUp() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        response = new Response(connection);
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(0, headers.size());
    }
    
    @Test
    public void testGetBody() {
        String body = response.getBody();
        assertNotNull(body);
        assertEquals("", body);
    }
    
    @Test
    public void testGetStream() {
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }
    
    @Test
    public void testGetHeader() {
        String header = response.getHeader("Content-Type");
        assertNull(header);
    }

    @Test
    public void testParseBodyContents() {
        String parsedBody = response.parseBodyContents();
        assertNotNull(parsedBody);
        assertEquals("", parsedBody);
    }
    
    @Test
    public void testParseHeaders() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, String> parsedHeaders = response.parseHeaders(connection);
        assertNotNull(parsedHeaders);
        assertEquals(0, parsedHeaders.size());
    }
    
    @Test
    public void testWasSuccessful() {
        assertTrue(response.wasSuccessful());
    }
}