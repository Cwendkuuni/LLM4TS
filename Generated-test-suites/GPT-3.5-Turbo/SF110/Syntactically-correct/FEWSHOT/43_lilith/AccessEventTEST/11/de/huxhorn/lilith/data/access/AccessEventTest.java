package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AccessEventTest {
    // Test methods for getTimeStamp
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());

        Long timeStamp = 1234567890L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    // Test methods for setTimeStamp
    @Test
    public void testSetTimeStamp() {
        AccessEvent event = new AccessEvent();

        Long timeStamp = 1234567890L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    // Test methods for getLoggerContext
    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getLoggerContext());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    // Test methods for setLoggerContext
    @Test
    public void testSetLoggerContext() {
        AccessEvent event = new AccessEvent();

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    // Rest of the test methods for the remaining getters/setters

}