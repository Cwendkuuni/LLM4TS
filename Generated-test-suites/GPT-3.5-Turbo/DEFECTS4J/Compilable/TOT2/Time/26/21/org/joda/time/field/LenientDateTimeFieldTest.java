package org.joda.time.field;
import static org.junit.Assert.*;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private Chronology base;
    private DateTimeField strictField;
    private LenientDateTimeField lenientField;

    @Before
    public void setUp() {
        base = ISOChronology.getInstance(DateTimeZone.UTC);
        strictField = base.hourOfDay();
        lenientField = new LenientDateTimeField(strictField, base);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = base.getDateTimeMillis(2021, 9, 10, 12, 30, 0, 0);
        int value = 15;
        
        long result = lenientField.set(instant, value);
        
        assertEquals(base.getDateTimeMillis(2021, 9, 10, 15, 30, 0, 0), result);
    }

    @Test
    public void testGetInstance() {
        DateTimeField result = LenientDateTimeField.getInstance(strictField, base);
        
        assertEquals(strictField, result);
    }
}