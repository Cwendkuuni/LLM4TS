import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(createMockConnection());
        assertEquals("Response body is empty", "", response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(createMockConnection());
        assertNotNull("Response stream is not null", response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(createMockConnection());
        assertEquals("Response code is correct", 200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(createMockConnection());
        Map<String, String> headers = response.getHeaders();
        assertTrue("Response headers are not null or empty", headers != null && !headers.isEmpty());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(createMockConnection());
        assertEquals("Specific header is correct", "application/json", response.getHeader("Content-Type"));
    }

    private HttpURLConnection createMockConnection() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        Map<String, List<String>> headerFields = new HashMap<>();
        headerFields.put("Content-Type", Arrays.asList("application/json"));
        when(connection.getHeaderFields()).thenReturn(headerFields);
        return connection;
    }
}