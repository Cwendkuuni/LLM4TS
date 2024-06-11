import org.joda.time.*;
import org.joda.time.field.LenientDateTimeField;
import org.junit.Test;

import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    private final DateTime now = new DateTime();

    // Test for getInstance method
    @Test
    public void testGetInstance() {
        DateTimeField field = now.dayOfWeek();
        Chronology base = now.getChronology();
        
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        
        assertNotNull(lenientField);
        assertNotEquals(field, lenientField);
        assertTrue(lenientField.isLenient());
        
        // Test null input
        assertNull(LenientDateTimeField.getInstance(null, base));
        
        // Test field already lenient
        DateTimeField alreadyLenientField = lenientField;
        assertEquals(alreadyLenientField, LenientDateTimeField.getInstance(alreadyLenientField, base));
    }

    // Test for isLenient method
    @Test
    public void testIsLenient() {
        DateTimeField field = now.dayOfYear();
        
        DateTimeField lenientField = new LenientDateTimeField(field, now.getChronology());
        
        assertTrue(lenientField.isLenient());
    }

    // Test for set method
    @Test
    public void testSet() {
        DateTimeField field = now.dayOfMonth();
        Chronology base = now.getChronology();
        
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
        
        long instant = now.getMillis();
        
        // Test typical use case
        int value1 = 15;
        long expectedInstant1 = base.withUTC().dayOfMonth().addTo(instant, 15);
        assertEquals(expectedInstant1, lenientField.set(instant, value1));
        
        // Test edge case: out of bounds value
        int value2 = 50;
        long expectedInstant2 = base.withUTC().dayOfMonth().addTo(instant, 50);
        assertEquals(expectedInstant2, lenientField.set(instant, value2));
        
        // Test error scenario: negative value
        int value3 = -5;
        long expectedInstant3 = base.withUTC().dayOfMonth().addTo(instant, -5);
        assertEquals(expectedInstant3, lenientField.set(instant, value3));
    }

}