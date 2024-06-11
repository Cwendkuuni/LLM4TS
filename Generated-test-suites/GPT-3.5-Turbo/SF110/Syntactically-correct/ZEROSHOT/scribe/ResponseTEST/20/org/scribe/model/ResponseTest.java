package org.scribe.model;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() {
        // Set up test data
        HttpURLConnection connection = new HttpURLConnection();
        // Set up connection properties and response code
        response = new Response(connection);
    }

    @Test
    public void testGetBody() {
        // Your test code
        String expectedBody = "";

        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        // Your test code
        InputStream expectedStream = null;

        assertEquals(expectedStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        // Your test code
        int expectedCode = 0;

        assertEquals(expectedCode, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        // Your test code
        Map<String, String> expectedHeaders = new HashMap<String, String>();

        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        // Your test code
        String expectedHeader = null;
        String name = "";

        assertEquals(expectedHeader, response.getHeader(name));
    }

}