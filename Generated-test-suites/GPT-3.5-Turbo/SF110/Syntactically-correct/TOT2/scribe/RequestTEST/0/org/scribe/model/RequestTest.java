package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class RequestTest {

    @Test
    public void testConstructor() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("https://www.example.com", request.getUrl());
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertTrue(request.getHeaders().containsKey("Content-Type"));
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "https://www.example.com");
        request.addBodyParameter("key", "value");
        assertTrue(request.getBodyParams().containsKey("key"));
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "https://www.example.com");
        request.addQuerystringParameter("key", "value");
        assertTrue(request.getQueryStringParams().containsKey("key"));
        assertEquals("value", request.getQueryStringParams().get("key"));
    }

    // Additional test cases for edge cases and exceptions can be added here

}