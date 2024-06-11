import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {
    
    @Test
    public void testConstructor() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            assertEquals(200, response.getCode());
            assertNotNull(response.getHeaders());
            assertTrue(response.getHeaders().isEmpty());
            assertNull(response.getBody());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testParseBodyContents() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            assertNotNull(response.getCode());
            assertNotNull(response.getHeaders());
            assertTrue(response.getHeaders().isEmpty());
            
            String body = response.parseBodyContents();
            assertNotNull(body);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testParseHeaders() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            assertNotNull(response.getCode());
            
            Map<String, String> headers = response.parseHeaders(connection);
            assertNotNull(headers);
            assertTrue(headers.isEmpty());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testWasSuccessful() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            assertEquals(200, response.getCode());
            
            boolean successful = response.wasSuccessful();
            assertTrue(successful);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testGetBody() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            
            String body = response.getBody();
            assertNotNull(body);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testGetStream() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testGetCode() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            
            int code = response.getCode();
            assertNotNull(code);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            
            Map<String, String> headers = response.getHeaders();
            assertNotNull(headers);
            assertTrue(headers.isEmpty());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    @Test
    public void testGetHeader() throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            Response response = new Response(connection);
            
            assertNotNull(response);
            assertNotNull(response.getStream());
            
            String header = response.getHeader("Content-Type");
            assertNull(header);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}