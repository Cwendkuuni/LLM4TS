package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.Chronology;
import org.joda.time.chrono.LenientChronology;
import org.joda.time.chrono.ISOChronology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    
    private static Chronology baseChronology;

    @BeforeClass
    public static void setUp() {
        baseChronology = ISOChronology.getInstanceUTC();
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = baseChronology.secondOfMinute();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, baseChronology);
        assertTrue(lenientField.isLenient());

        DateTimeField strictField = LenientDateTimeField.getInstance(lenientField, baseChronology);
        assertTrue(strictField.isLenient());

        DateTimeField nullField = LenientDateTimeField.getInstance(null, baseChronology);
        assertEquals(null, nullField);
    }

    @Test
    public void testIsLenient() {
        DateTimeField field = baseChronology.secondOfMinute();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, baseChronology);
        assertTrue(lenientField.isLenient());

        DateTimeField strictField = LenientDateTimeField.getInstance(lenientField, baseChronology);
        assertTrue(strictField.isLenient());
    }

    @Test
    public void testSet() {
        DateTimeField field = baseChronology.secondOfMinute();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, baseChronology);

        long instant = baseChronology.getDateTimeMillis(2021, 10, 1, 0, 0, 0, 0);
        int originalValue = lenientField.get(instant);
        assertEquals(0, originalValue);

        long newInstant = lenientField.set(instant, 12);
        int newValue = lenientField.get(newInstant);
        assertEquals(12, newValue);

        long outOfBoundsInstant = lenientField.set(instant, 70);
        int outOfBoundsValue = lenientField.get(outOfBoundsInstant);
        assertEquals(6, outOfBoundsValue);
    }
}