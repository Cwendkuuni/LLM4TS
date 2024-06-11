package de.huxhorn.lilith.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AccessEventTest {
    @Test
    public void testGetTimeStamp() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getTimeStamp());

        Long timeStamp = 123456789L;
        event.setTimeStamp(timeStamp);
        assertEquals(timeStamp, event.getTimeStamp());
    }

    @Test
    public void testGetLoggerContext() {
        AccessEvent event = new AccessEvent();
        assertNull(event.getLoggerContext());

        LoggerContext loggerContext = new LoggerContext();
        event.setLoggerContext(loggerContext);
        assertEquals(loggerContext, event.getLoggerContext());
    }

    // Other test methods for the remaining getter and setter methods

    @Test
    public void testEquals() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertTrue(event1.equals(event1)); // Reflexive
        assertFalse(event1.equals(null)); // Null check
        assertFalse(event1.equals(new Object())); // Type check

        // Two newly created objects are equal
        assertTrue(event1.equals(event2));

        // Modify one property of event1
        event1.setTimeStamp(123456789L);

        // Objects with different property values are not equal
        assertFalse(event1.equals(event2));

        // Modify the same property of event2 to be the same as event1
        event2.setTimeStamp(123456789L);

        // Objects with the same property values are equal
        assertTrue(event1.equals(event2));

        // Test with other properties in a similar way

    }

    @Test
    public void testHashCode() {
        AccessEvent event1 = new AccessEvent();
        AccessEvent event2 = new AccessEvent();

        assertEquals(event1.hashCode(), event2.hashCode()); // Hash codes of two newly created objects are equal

        // Modify one property of event1
        event1.setTimeStamp(123456789L);

        // Hash codes of objects with different property values are different
        assertNotEquals(event1.hashCode(), event2.hashCode());

        // Modify the same property of event2 to be the same as event1
        event2.setTimeStamp(123456789L);

        // Hash codes of objects with the same property values are equal
        assertEquals(event1.hashCode(), event2.hashCode());

        // Test with other properties in a similar way

    }

    @Test
    public void testToString() {
        AccessEvent event = new AccessEvent();
        assertEquals("AccessEvent[loggerContext=null, timeStamp=null]", event.toString());

        event.setLoggerContext(new LoggerContext());
        event.setTimeStamp(123456789L);
        assertEquals("AccessEvent[loggerContext=LoggerContext@<hashCode>, timeStamp=123456789]", event.toString());
    }
}