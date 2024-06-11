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
        // Test case when body is not null
        Response response = new Response();
        response.body = "Test Body";
        String expectedBody = "Test Body";
        String actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);

        // Test case when body is null
        response.body = null;
        expectedBody = "";
        actualBody = response.getBody();
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void testGetStream() {
        // Test case when stream is not null
        Response response = new Response();
        InputStream expectedStream = new ByteArrayInputStream("Test Stream".getBytes());
        response.stream = expectedStream;
        InputStream actualStream = response.getStream();
        assertEquals(expectedStream, actualStream);

        // Test case when stream is null
        response.stream = null;
        expectedStream = null;
        actualStream = response.getStream();
        assertEquals(expectedStream, actualStream);
    }

    @Test
    public void testGetCode() {
        Response response = new Response();
        response.code = 200;
        int expectedCode = 200;
        int actualCode = response.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response();
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("Content-Type", "application/json");
        expectedHeaders.put("Cache-Control", "no-cache");
        response.headers = expectedHeaders;
        Map<String, String> actualHeaders = response.getHeaders();
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGetHeader() {
        Response response = new Response();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cache-Control", "no-cache");
        response.headers = headers;
        String expectedHeaderValue = "application/json";
        String actualHeaderValue = response.getHeader("Content-Type");
        assertEquals(expectedHeaderValue, actualHeaderValue);
    }
}