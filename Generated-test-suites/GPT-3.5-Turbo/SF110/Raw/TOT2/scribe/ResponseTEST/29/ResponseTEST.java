package org.scribe.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.net.HttpURLConnection;
import org.scribe.model.Response;

public class ResponseTest {

    private HttpURLConnection mockConnection;
    private Response response;

    @Before
    public void setUp() {
        mockConnection = Mockito.mock(HttpURLConnection.class);
        response = new Response(mockConnection);
    }

    @Test
    public void testParseBodyContents() {
        String expectedBody = "test body";
        response.body = expectedBody;
        assertEquals(expectedBody, response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");

        Mockito.when(mockConnection.getHeaderFields()).thenReturn(expectedHeaders);

        assertEquals(expectedHeaders, response.parseHeaders(mockConnection));
    }

    @Test
    public void testWasSuccessful() {
        response.code = 200;
        assertTrue(response.wasSuccessful());

        response.code = 400;
        assertFalse(response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        response.body = "test body";
        assertEquals("test body", response.getBody());
    }

    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        response.code = 200;
        assertEquals(200, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");

        response.headers = expectedHeaders;

        assertEquals(expectedHeaders, response.getHeaders());
    }

    @Test
    public void testGetHeader() {
        String headerName = "Content-Type";
        String expectedValue = "application/json";

        Map<String, String> headers = new HashMap<>();
        headers.put(headerName, expectedValue);

        response.headers = headers;

        assertEquals(expectedValue, response.getHeader(headerName));
    }
}