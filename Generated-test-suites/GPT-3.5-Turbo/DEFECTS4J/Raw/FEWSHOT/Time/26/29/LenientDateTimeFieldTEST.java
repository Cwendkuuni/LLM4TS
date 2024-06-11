package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.ISOChronology;
import org.junit.BeforeClass;
import org.junit.Test;

public class LenientDateTimeFieldTest {
    private static Chronology chronology;
    private static DateTimeField field;
    private static LenientDateTimeField lenientField;

    @BeforeClass
    public static void setUp() {
        chronology = ISOChronology.getInstanceUTC();
        field = chronology.minuteOfDay();
        lenientField = (LenientDateTimeField) LenientDateTimeField.getInstance(field, chronology);
    }

    @Test
    public void testGetInstance() {
        DateTimeField instance = LenientDateTimeField.getInstance(field, chronology);
        assertNotNull(instance);
        assertEquals(field, instance);
    }

    @Test
    public void testGetInstance_NullField() {
        DateTimeField instance = LenientDateTimeField.getInstance(null, chronology);
        assertNull(instance);
    }

    @Test
    public void testGetInstance_LenientField() {
        DateTimeField instance = LenientDateTimeField.getInstance(lenientField, chronology);
        assertNotNull(instance);
        assertEquals(lenientField, instance);
    }

    @Test
    public void testGetInstance_StrictField() {
        DateTimeField strictField = new StrictDateTimeField(field, chronology);
        DateTimeField instance = LenientDateTimeField.getInstance(strictField, chronology);
        assertNotNull(instance);
        assertEquals(strictField.getWrappedField(), instance);
    }

    @Test
    public void testIsLenient() {
        assertEquals(true, lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = 30;

        assertEquals(instant + (30 * 60 * 1000), lenientField.set(instant, value));
        assertEquals(instant - (30 * 60 * 1000), lenientField.set(instant, -value));
    }

    @Test
    public void testSet_OutOfBounds() {
        long instant = DateTimeUtils.currentTimeMillis();
        int value = field.getMaximumValue() + 1;

        assertEquals(instant + (value * 60 * 1000), lenientField.set(instant, value));
    }
}