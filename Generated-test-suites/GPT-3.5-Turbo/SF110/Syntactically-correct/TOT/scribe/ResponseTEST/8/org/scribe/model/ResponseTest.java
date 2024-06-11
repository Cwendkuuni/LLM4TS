import org.junit.Test;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testConstructor_SuccessfulConnection() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            assertEquals(200, response.getCode());
            assertNotNull(response.getHeaders());
            assertNull(response.getBody());
            assertNotNull(response.getStream());
            assertNotNull(response.getHeader("Content-Type"));
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testConstructor_ConnectionError() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://nonexistenturl.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            assertEquals(404, response.getCode());
            assertNotNull(response.getHeaders());
            assertNotNull(response.getBody());
            assertNotNull(response.getStream());
            assertNull(response.getHeader("Content-Type"));
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetBody_BodyNotNull() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            String body = response.getBody();
            
            assertNotNull(body);
            assertTrue(body.contains("Example Domain"));
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetBody_BodyNull() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            response.getStream().close();
            
            String body = response.getBody();
            
            assertNull(body);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetStream_StreamNotNull() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            InputStream stream = response.getStream();
            
            assertNotNull(stream);
            assertEquals(200, stream.read());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetCode() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            assertEquals(200, response.getCode());
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetHeaders() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            Map<String, String> headers = response.getHeaders();
            
            assertNotNull(headers);
            assertEquals("text/html; charset=UTF-8", headers.get("Content-Type"));
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetHeader_ExistingHeader() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            String headerValue = response.getHeader("Content-Type");
            
            assertNotNull(headerValue);
            assertEquals("text/html; charset=UTF-8", headerValue);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
    
    @Test
    public void testGetHeader_NonExistingHeader() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://example.com").openConnection();
            connection.setRequestMethod("GET");
            
            Response response = new Response(connection);
            
            String headerValue = response.getHeader("Non-Existing");
            
            assertNull(headerValue);
        } catch (IOException e) {
            fail("IOException should not be thrown.");
        }
    }
}