
package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        String body = response.getBody();
        assertNotNull(body);
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(null);
        InputStream stream = response.getStream();
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(null);
        int code = response.getCode();
        assertEquals(0, code);
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        Map<String, String> headers = response.getHeaders();
        assertNotNull(headers);
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        String header = response.getHeader("ExampleHeader");
        assertNull(header);
    }
}
