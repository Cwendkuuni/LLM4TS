package org.joda.time.field;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.LenientChronology;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    @Test
    public void testIsLenient() {
        DateTimeField field = new LenientDateTimeField(null, null);
        assertEquals(true, field.isLenient());
    }
    
    @Test
    public void testSet() {
        Chronology base = new LenientChronology(null, null);
        DateTimeField field = new LenientDateTimeField(null, base);
        long instant = 0;
        int value = 1;
        long result = field.set(instant, value);
        assertEquals(1, result);
    }
}