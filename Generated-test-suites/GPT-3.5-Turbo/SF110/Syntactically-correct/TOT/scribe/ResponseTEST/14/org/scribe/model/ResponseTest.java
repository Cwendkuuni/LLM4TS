package org.scribe.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() {
        response = new Response(null);
    }

    @Test
    public void testParseBodyContents() {
        // typical use case
        response.getStream();
        assertEquals("Expected body contents should be returned", "", response.parseBodyContents());

        // edge case - empty stream
        response = new Response(null);
        assertEquals("Expected empty body contents should be returned", "", response.parseBodyContents());

        // error scenario - null stream
        response = new Response(null);
        response.getStream();
        response.parseBodyContents();
        response.getStream();
        assertNull("Expected null body contents should be returned", response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        // typical use case
        Response response = new Response(null);
        assertEquals("Expected empty headers should be returned", 0, response.parseHeaders(null).size());

        // edge case - single header
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://example.com");
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Test-Header", "Value");
            response = new Response(conn);
        } catch (Exception e) {
            fail("Exception thrown during setup");
        }
        assertEquals("Expected single header should be returned", 1, response.parseHeaders(conn).size());

        // error scenario - null connection
        response = new Response(null);
        assertNull("Expected null headers should be returned", response.parseHeaders(null));
    }

    @Test
    public void testWasSuccessful() {
        // typical use case
        response = new Response(null);
        response.getCode();
        assertTrue("Expected response to be successful", response.wasSuccessful());

        // edge case - code within success range
        response = new Response(null);
        response.code = 299;
        assertTrue("Expected response to be successful", response.wasSuccessful());

        // edge case - code at success boundary
        response = new Response(null);
        response.code = 200;
        assertTrue("Expected response to be successful", response.wasSuccessful());

        // edge case - code outside success range
        response = new Response(null);
        response.code = 400;
        assertFalse("Expected response to not be successful", response.wasSuccessful());

        // error scenario - code less than 200
        response = new Response(null);
        response.code = 199;
        assertFalse("Expected response to not be successful", response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        // typical use case
        response = new Response(null);
        response.parseBodyContents();
        assertEquals("Expected body contents should be returned", "", response.getBody());

        // edge case - null body
        response = new Response(null);
        assertEquals("Expected null body should be returned", null, response.getBody());
    }

    @Test
    public void testGetStream() {
        // typical use case
        response = new Response(null);
        assertNotNull("Expected non-null stream should be returned", response.getStream());
    }

    @Test
    public void testGetCode() {
        // typical use case
        response = new Response(null);
        assertEquals("Expected code should be returned", 0, response.getCode());

        // edge case - positive non-zero code
        response = new Response(null);
        response.code = 123;
        assertEquals("Expected code should be returned", 123, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        // typical use case
        response = new Response(null);
        assertEquals("Expected empty headers should be returned", 0, response.getHeaders().size());

        // edge case - non-empty headers
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://example.com");
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Test-Header", "Value");
            response = new Response(conn);
        } catch (Exception e) {
            fail("Exception thrown during setup");
        }
        assertEquals("Expected single header should be returned", 1, response.getHeaders().size());
    }

    @Test
    public void testGetHeader() {
        // typical use case
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://example.com");
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Test-Header", "Value");
            response = new Response(conn);
        } catch (Exception e) {
            fail("Exception thrown during setup");
        }
        assertEquals("Expected header value should be returned", "Value", response.getHeader("Test-Header"));

        // edge case - non-existent header
        response = new Response(null);
        assertNull("Expected null header value should be returned", response.getHeader("Non-Existent-Header"));
    }
}