package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        response.getBody();
        assertNotNull(response.getBody());
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
        assertNull(response.getHeader("TestHeader"));
    }

    @Test
    public void testParseHeaders() {
        Response response = new Response(null);
        assertNotNull(response.parseHeaders(null));
    }

    @Test
    public void testWasSuccessful() {
        Response response = new Response(null);
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testParseBodyContents() {
        Response response = new Response(null);
        assertNotNull(response.parseBodyContents());
    }
}