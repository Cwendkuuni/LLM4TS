package org.joda.time.field.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFields;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the LenientDateTimeField class.
 */
public class LenientDateTimeFieldTest {

    private static final Chronology DATE_TIME_CHRONO = DateTimeFields.UTC;

    @Test
    public void testLenientDateTimeField() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        assertTrue(lenientField.isLenient());
        assertEquals(lenientField, LenientDateTimeField.getInstance(lenientField, DATE_TIME_CHRONO));
        assertNotSame(lenientField, field);
        assertNotSame(lenientField, new StrictDateTimeField(field));
    }

    @Test
    public void testSetOutOfBoundsValue() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 0, DATE_TIME_CHRONO);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), 60);
        assertEquals(dateTime.getMillis(), result);
        assertEquals(60, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueNegative() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 0, DATE_TIME_CHRONO);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), -10);
        assertEquals(dateTime.getMillis(), result);
        assertEquals(55, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueMaxValue() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 59, DATE_TIME_CHRONO);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), 61);
        assertEquals(dateTime.getMillis(), result);
        assertEquals(60, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueMaxValuePlusOne() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 59, DATE_TIME_CHRONO);
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), 62);
        assertEquals(dateTime.getMillis() + 60_000, result);
        assertEquals(1, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueLarge() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 59, DATE_TIME_CHRONO).withZoneRetainFields(DateTimeZone.forOffsetHours(12));
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), 13 * 60);
        assertEquals(dateTime.getMillis() + 13 * 60 * 60_000, result);
        assertEquals(13, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueLargeNegative() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 59, DATE_TIME_CHRONO).withZoneRetainFields(DateTimeZone.forOffsetHours(12));
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), -13 * 60);
        assertEquals(dateTime.getMillis() - 13 * 60 * 60_000, result);
        assertEquals(13, lenientField.get(dateTime.getMillis()));
    }

    @Test
    public void testSetOutOfBoundsValueWithTimeZone() {
        DateTimeField field = DateTimeFields.MINUTE_OF_HOUR;
        MutableDateTime dateTime = new MutableDateTime(0, 0, 0, 0, 55, 59, DateTimeZone.forOffsetHours(12));
        LenientDateTimeField lenientField = LenientDateTimeField.getInstance(field, DATE_TIME_CHRONO);
        long result = lenientField.set(dateTime.getMillis(), 24 * 60);
        assertTrue(result > dateTime.getMillis());
        assertEquals(1, lenientField.get(result));
    }
}