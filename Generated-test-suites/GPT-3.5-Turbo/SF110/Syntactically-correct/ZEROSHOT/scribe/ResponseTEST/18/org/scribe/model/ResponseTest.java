package org.scribe.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.*;
import java.io.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Create Response instance
        Response response = new Response(null);
        
        // Test when body is null
        assertNull(response.getBody());
        
        // Test when body is not null
        response = new Response(null);
        response.parseBodyContents();
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        // Create Response instance
        Response response = new Response(null);
        
        // Test when stream is not null
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        // Create Response instance
        Response response = new Response(null);
        
        // Test getCode method
        assertEquals(0, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        // Create Response instance
        Response response = new Response(null);
        
        // Test getHeaders method
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        // Create Response instance
        Response response = new Response(null);
        
        // Test getHeader method
        assertNull(response.getHeader("Name"));
    }
}