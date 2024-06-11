package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.HashMap;

public class AccessEventTest {

    @Test
    public void testGettersAndSetters() {
        AccessEvent event = new AccessEvent();

        // Set values
        event.setTimeStamp(1234567890L);
        event.setLoggerContext(new LoggerContext());
        event.setRequestURI("http://example.com/path");
        event.setRequestURL("http://example.com/path");
        event.setRemoteHost("localhost");
        event.setRemoteUser("user");
        event.setProtocol("HTTP/1.1");
        event.setMethod("GET");
        event.setServerName("localhost");
        event.setRemoteAddress("127.0.0.1");
        event.setRequestHeaders(new HashMap<String, String>());
        event.setResponseHeaders(new HashMap<String, String>());
        event.setRequestParameters(new HashMap<String, String[]>());
        event.setLocalPort(8080);
        event.setStatusCode(200);

        // Verify values
        assertEquals(1234567890L, event.getTimeStamp().longValue());
        assertNotNull(event.getLoggerContext());
        assertEquals("http://example.com/path", event.getRequestURI());
        assertEquals("http://example.com/path", event.getRequestURL());
        assertEquals("localhost", event.getRemoteHost());
        assertEquals("user", event.getRemoteUser());
        assertEquals("HTTP/1.1", event.getProtocol());
        assertEquals("GET", event.getMethod());
        assertEquals("localhost", event.getServerName());
        assertEquals("127.0.0.1", event.getRemoteAddress());
        assertNotNull(event.getRequestHeaders());
        assertNotNull(event.getResponseHeaders());
        assertNotNull(event.getRequestParameters());
        assertEquals(8080, event.getLocalPort());
        assertEquals(200, event.getStatusCode());
    }

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        // Verify equality for both null objects
        assertTrue(event1.equals(event2));

        // Verify equality for same object
        assertTrue(event1.equals(event1));

        // Verify equality for different objects with same values
        event1.setTimeStamp(1234567890L);
        event2.setTimeStamp(1234567890L);
        assertTrue(event1.equals(event2));

        // Verify inequality for different objects with different values
        event1.setTimeStamp(1234567890L);
        event2.setTimeStamp(9876543210L);
        assertFalse(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        // Verify equal hash codes for both null objects
        assertEquals(event1.hashCode(), event2.hashCode());

        // Verify equal hash codes for same object
        assertEquals(event1.hashCode(), event1.hashCode());

        // Verify equal hash codes for different objects with same values
        event1.setTimeStamp(1234567890L);
        event2.setTimeStamp(1234567890L);
        assertEquals(event1.hashCode(), event2.hashCode());

        // Verify different hash codes for different objects with different values
        event1.setTimeStamp(1234567890L);
        event2.setTimeStamp(9876543210L);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();

        // Verify string representation
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());

        // Set values
        event.setTimeStamp(1234567890L);
        event.setLoggerContext(new LoggerContext());

        // Verify updated string representation
        assertEquals("AccessEvent[loggerContext=com.example.LoggerContext@1234567890, timeStamp=1234567890]", event.toString());
    }
}