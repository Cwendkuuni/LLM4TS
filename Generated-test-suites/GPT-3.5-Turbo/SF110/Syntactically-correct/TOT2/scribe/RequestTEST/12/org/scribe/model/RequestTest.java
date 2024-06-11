package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.*;

public class RequestTest {

    private Request request;

    @Before
    public void setUp() {
        request = new Request(Verb.GET, "https://www.example.com");
    }

    @Test
    public void testSend() {
        // Typical use case
        Response response = request.send();
        assertNotNull(response);
    }

    @Test
    public void testAddHeader() {
        // Typical use case
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        // Typical use case
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        // Typical use case
        request.addQuerystringParameter("param", "123");
        assertEquals("123", request.getQueryStringParams().get("param"));
    }

    // More test cases for other public methods...

}