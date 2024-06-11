package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

public class ResponseTest {

    private Response response;

    @Before
    public void setUp() throws IOException {
        HttpURLConnection connection = null;
        //Set up HttpURLConnection and pass it to Response constructor
        this.response = new Response(connection);
    }

    @Test
    public void testParseBodyContents() {
        //Test for parsing body contents
        //Initialize stream before running this test
        //Expected body should be equal to the contents of the stream
        assertEquals(response.getStream(), response.parseBodyContents());
    }

    @Test
    public void testParseHeaders() {
        //Test for parsing headers
        //Expected headers should be equal to the headers obtained from HttpURLConnection
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        HttpURLConnection connection = null;
        for (final String key : connection.getHeaderFields().keySet()) {
            expectedHeaders.put(key, connection.getHeaderFields().get(key).get(0));
        }
        assertEquals(expectedHeaders, response.parseHeaders(connection));
    }

    @Test
    public void testWasSuccessful() {
        //Test for checking if the response was successful
        //Code should be between 200 and 399 (inclusive) for a successful response
        boolean expected = (response.getCode() >= 200 && response.getCode() < 400);
        assertEquals(expected, response.wasSuccessful());
    }

    @Test
    public void testGetBody() {
        //Test for getting the body of the response
        //Expected body should be equal to the body obtained from parseBodyContents()
        String expectedBody = (response.getBody() != null) ? response.getBody() : response.parseBodyContents();
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void testGetStream() {
        //Test for getting the stream of the response
        //Expected stream should be equal to the stream passed to Response constructor
        InputStream expectedStream = null; //Set the expected stream here
        assertEquals(expectedStream, response.getStream());
    }

    @Test
    public void testGetCode() {
        //Test for getting the code of the response
        //Expected code should be equal to the code obtained from HttpURLConnection
        int expectedCode = 200; //Set the expected code here
        assertEquals(expectedCode, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        //Test for getting the headers of the response
        //Expected headers should be equal to the headers obtained from parseHeaders()
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("key", "value"); //Set the expected headers here
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        //Test for getting a specific header of the response
        //Expected header should be equal to the header obtained from the headers map
        String expectedHeader = "value"; //Set the expected header value here
        String headerName = "key"; //Set the header name here
        assertEquals(expectedHeader, response.getHeader(headerName));
    }
}