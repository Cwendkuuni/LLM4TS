import org.junit.Test;
import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.UnknownHostException;
import org.scribe.utils.StreamUtils;

public class ResponseTest {
    @Test
    public void testResponseConstructor() throws IOException {
        HttpURLConnection connection = null;
        Response response = new Response(connection);
        
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        
        response.parseBodyContents();
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        HttpURLConnection connection = null;
        
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        assertEquals(expectedHeaders, response.parseHeaders(connection));
    }
    
    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        response.setCode(200);
        assertTrue(response.wasSuccessful());
        
        response.setCode(400);
        assertFalse(response.wasSuccessful());
    }
    
    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.setBody("test body");

        assertEquals("test body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(null);
        InputStream stream = null;
        response.setStream(stream);

        assertEquals(stream, response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.setCode(200);

        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>();
        response.setHeaders(headers);

        assertEquals(headers, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        assertEquals("application/json", response.getHeader("Content-Type"));
    }
}