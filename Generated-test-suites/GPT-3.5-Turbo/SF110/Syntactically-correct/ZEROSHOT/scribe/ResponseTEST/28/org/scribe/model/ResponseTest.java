package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    private HttpURLConnection connection;
    private Response response;

    @Before
    public void setUp() throws IOException {
        URL url = new URL("http://example.com");
        connection = (HttpURLConnection) url.openConnection();
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        String body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isEmpty());
    }

    @Test
    public void testGetStream() {
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }

    @Test
    public void testGetCode() {
        int code = response.getCode();
        assertTrue(code >= 200 && code < 400);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
        assertFalse(headers.isEmpty());
    }
    
    @Test
    public void testGetHeader() {
        String name = "Content-Type";
        String header = response.getHeader(name);
        assertNotNull(header);
    }
    
    @After
    public void tearDown() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}