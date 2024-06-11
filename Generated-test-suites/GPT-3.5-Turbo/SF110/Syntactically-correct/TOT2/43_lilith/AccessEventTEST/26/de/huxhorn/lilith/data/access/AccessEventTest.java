package de.huxhorn.lilith.data.access;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {

    private AccessEvent accessEvent;

    @Before
    public void setUp() {
        accessEvent = new AccessEvent();
    }

    @Test
    public void testGetSetTimeStamp() {
        Long timestamp = 123456789L;
        accessEvent.setTimeStamp(timestamp);
        assertEquals(timestamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetSetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetSetRequestURI() {
        String requestURI = "/test";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    @Test
    public void testGetSetRequestURL() {
        String requestURL = "http://example.com/test";
        accessEvent.setRequestURL(requestURL);
        assertEquals(requestURL, accessEvent.getRequestURL());
    }

    // Other test cases for the remaining methods can be added here

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setLocalPort(8080);
        event2.setLocalPort(8081);

        assertFalse(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        Map<AccessEvent, String> map = new HashMap<>();
        AccessEvent event = new AccessEvent();
        
        event.setRemoteHost("localhost");

        map.put(event, "test");

        assertTrue(map.containsKey(event));
    }

    @Test
    public void testToString() {
        LoggerContext loggerContext = new LoggerContext();
        Long timestamp = 123456789L;
        
        accessEvent.setLoggerContext(loggerContext);
        accessEvent.setTimeStamp(timestamp);

        String expectedString = "AccessEvent[loggerContext=" + loggerContext + ", timeStamp=" + timestamp + "]";
        assertEquals(expectedString, accessEvent.toString());
    }
}