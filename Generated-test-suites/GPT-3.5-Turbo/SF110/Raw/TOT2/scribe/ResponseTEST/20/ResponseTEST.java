package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.scribe.model.Response;

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
        
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        
        assertEquals(0, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        
        assertNull(response.getHeader("Test"));
    }
}