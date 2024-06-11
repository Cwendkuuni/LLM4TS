package org.scribe.model;

import org.junit.*;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Test with non-null body
        Response response = new Response(null);
        response.body = "Test body";
        assertEquals("Test body", response.getBody());

        // Test with null body
        Response response2 = new Response(null);
        response2.body = null;
        response2.parseBodyContents();
        assertNull(response2.getBody());
    }

    @Test
    public void testGetStream() {
        // Test with non-null stream
        Response response = new Response(null);
        response.stream = // Set the stream here;
        assertNotNull(response.getStream());

        // Test with null stream
        Response response2 = new Response(null);
        response2.stream = null;
        assertNull(response2.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = new Response(null);
        response.code = 200;
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(null);
        response.headers = // Set the headers here;
        assertNotNull(response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(null);
        response.headers = // Set the headers here;
        assertEquals("VALUE", response.getHeader("KEY"));
    }
}