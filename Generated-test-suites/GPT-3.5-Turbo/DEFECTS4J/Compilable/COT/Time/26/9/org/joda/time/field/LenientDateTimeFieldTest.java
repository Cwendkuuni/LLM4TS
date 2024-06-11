package org.joda.time.field;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(null, null);
        assertTrue(field.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = new LenientDateTimeField(null, null);
        long result = field.set(0, 1);
        assertEquals(1, result);
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = new LenientDateTimeField(null, null);
        DateTimeField result = LenientDateTimeField.getInstance(field, null);
        assertSame(field, result);
    }
}