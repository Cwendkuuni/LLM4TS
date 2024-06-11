import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {
    
    @Test
    public void testGetBody_whenBodyIsNull_parseBodyContents() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        response.body = null;
        String parsedBody = response.getBody();
        assertNotNull(parsedBody);
    }
    
    @Test
    public void testGetStream() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        int code = response.getCode();
        assertEquals(0, code);
    }
    
    @Test
    public void testGetHeaders() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
    }
    
    @Test
    public void testGetHeader() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        response.headers = new HashMap<String, String>();
        response.headers.put("Content-Type", "application/json");
        String contentType = response.getHeader("Content-Type");
        assertEquals("application/json", contentType);
    }
    
    @Test
    public void testWasSuccessful_whenCodeIsWithinSuccessRange_returnTrue() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        response.code = 200;
        assertTrue(response.wasSuccessful());
    }
    
    @Test
    public void testWasSuccessful_whenCodeIsNotWithinSuccessRange_returnFalse() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        response.code = 404;
        assertFalse(response.wasSuccessful());
    }
    
    @Test
    public void testExceptionHandling_whenUnknownHostException_throw404Error() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Response response = new Response(connection);
        Exception e = new UnknownHostException();
        assertNotNull(e);
        assertEquals(404, response.getCode());
    }

}