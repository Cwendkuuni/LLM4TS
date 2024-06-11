package de.huxhorn.lilith.data.access.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {
    private AccessEvent event;

    @Before
    public void setUp() {
        event = new AccessEvent();
    }

    @Test
    public void testTimeStamp() {
        assertNull(event.getTimeStamp());

        event.setTimeStamp(1234L);
        assertEquals(Long.valueOf(1234L), event.getTimeStamp());
    }

    @Test
    public void testLoggerContext() {
        assertNull(event.getLoggerContext());

        event.setLoggerContext(null);
        assertNull(event.getLoggerContext());

        event.setLoggerContext(new LoggerContext());
        assertNotNull(event.getLoggerContext());
    }

    @Test
    public void testRequestURI() {
        assertNull(event.getRequestURI());

        event.setRequestURI("/path");
        assertEquals("/path", event.getRequestURI());
    }

    @Test
    public void testRequestURL() {
        assertNull(event.getRequestURL());

        event.setRequestURL("http://www.example.com");
        assertEquals("http://www.example.com", event.getRequestURL());
    }

    @Test
    public void testRemoteHost() {
        assertNull(event.getRemoteHost());

        event.setRemoteHost("example.com");
        assertEquals("example.com", event.getRemoteHost());
    }

    @Test
    public void testRemoteUser() {
        assertNull(event.getRemoteUser());

        event.setRemoteUser("user");
        assertEquals("user", event.getRemoteUser());
    }

    @Test
    public void testProtocol() {
        assertNull(event.getProtocol());

        event.setProtocol("HTTP/1.1");
        assertEquals("HTTP/1.1", event.getProtocol());
    }

    @Test
    public void testMethod() {
        assertNull(event.getMethod());

        event.setMethod("GET");
        assertEquals("GET", event.getMethod());
    }

    @Test
    public void testServerName() {
        assertNull(event.getServerName());

        event.setServerName("localhost");
        assertEquals("localhost", event.getServerName());
    }

    @Test
    public void testRemoteAddress() {
        assertNull(event.getRemoteAddress());

        event.setRemoteAddress("127.0.0.1");
        assertEquals("127.0.0.1", event.getRemoteAddress());
    }

    @Test
    public void testRequestHeaders() {
        assertNull(event.getRequestHeaders());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        event.setRequestHeaders(headers);
        assertEquals(headers, event.getRequestHeaders());
    }

    @Test
    public void testResponseHeaders() {
        assertNull(event.getResponseHeaders());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        event.setResponseHeaders(headers);
        assertEquals(headers, event.getResponseHeaders());
    }

    @Test
    public void testRequestParameters() {
        assertNull(event.getRequestParameters());

        HashMap<String, String[]> parameters = new HashMap<>();
        parameters.put("id", new String[] { "1" });

        event.setRequestParameters(parameters);
        assertEquals(parameters, event.getRequestParameters());
    }

    @Test
    public void testLocalPort() {
        assertEquals(0, event.getLocalPort());

        event.setLocalPort(8080);
        assertEquals(8080, event.getLocalPort());
    }

    @Test
    public void testStatusCode() {
        assertEquals(0, event.getStatusCode());

        event.setStatusCode(200);
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertTrue(event1.equals(event2));

        event1.setRequestURI("/path");
        assertFalse(event1.equals(event2));

        event2.setRequestURI("/path");
        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertEquals(event1.hashCode(), event2.hashCode());

        event1.setRequestURI("/path");
        assertNotEquals(event1.hashCode(), event2.hashCode());

        event2.setRequestURI("/path");
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        event.setRequestURI("/path");
        String expected = "AccessEvent[loggerContext=null, timeStamp=null, requestURI=/path]";
        assertEquals(expected, event.toString());
    }
}