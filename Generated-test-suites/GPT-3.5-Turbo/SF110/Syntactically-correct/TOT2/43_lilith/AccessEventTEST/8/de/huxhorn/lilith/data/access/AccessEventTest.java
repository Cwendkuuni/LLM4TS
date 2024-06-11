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
    public void testGetTimeStamp() {
        Long timeStamp = 123456789L;
        accessEvent.setTimeStamp(timeStamp);
        assertEquals(timeStamp, accessEvent.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        LoggerContext loggerContext = new LoggerContext();
        accessEvent.setLoggerContext(loggerContext);
        assertEquals(loggerContext, accessEvent.getLoggerContext());
    }

    @Test
    public void testGetRequestURI() {
        String requestURI = "/example";
        accessEvent.setRequestURI(requestURI);
        assertEquals(requestURI, accessEvent.getRequestURI());
    }

    // Add more individual test cases for each method here

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        event1.setTimeStamp(123456789L);
        event1.setLoggerContext(new LoggerContext());
        event1.setRequestURI("/example");

        event2.setTimeStamp(123456789L);
        event2.setLoggerContext(new LoggerContext());
        event2.setRequestURI("/example");

        assertTrue(event1.equals(event2));
    }

    @Test
    public void testHashCode() {
        Map<AccessEvent, String> map = new HashMap<>();
        AccessEvent event = new AccessEvent();
        map.put(event, "test");

        assertEquals("test", map.get(event));
    }

    @Test
    public void testToString() {
        accessEvent.setLoggerContext(new LoggerContext());
        accessEvent.setTimeStamp(123456789L);

        assertEquals("AccessEvent[loggerContext=LoggerContext[], timeStamp=123456789]", accessEvent.toString());
    }
}