import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = createResponse("body");
        assertEquals("body", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = createResponse("body");
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = createResponse("body");
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = createResponse("body");
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
    }
    
    @Test
    public void testGetHeader() {
        Response response = createResponse("body");
        response.getHeaders().put("Content-Type", "application/json");
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
    
    private Response createResponse(String body) {
        Response response = null;
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com"));
            response = new Response(connection);
            response.body = body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = createResponse("body");
        assertEquals("body", response.parseBodyContents());
    }
    
    @Test
    public void testParseHeaders() {
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com"));
            Response response = new Response(connection);
            Map<String, String> headers = response.parseHeaders(connection);
            assertEquals(0, headers.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testWasSuccessful() {
        Response response = createResponse("body");
        assertTrue(response.wasSuccessful());
    }
    
    @Test
    public void testWasNotSuccessful() {
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com"));
            Response response = new Response(connection);
            assertFalse(response.wasSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUnknownHostException() {
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://unknownhost"));
            Response response = new Response(connection);
            assertEquals(404, response.getCode());
            assertEquals("", response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}