package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

public class UnsupportedDurationFieldTest {

    @org.junit.Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @org.junit.Test
    public void testIsSupported() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(unsupported.isSupported());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1L;
        unsupported.getValue(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1L;
        unsupported.getValueAsLong(duration);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValue(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long duration = 1L;
        long instant = 1L;
        unsupported.getValueAsLong(duration, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 1;
        unsupported.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 1L;
        unsupported.getMillis(value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int value = 1;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndLongInstant() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long value = 1L;
        long instant = 1L;
        unsupported.getMillis(value, instant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1L;
        int value = 1;
        unsupported.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValue() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long instant = 1L;
        long value = 1L;
        unsupported.add(instant, value);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long minuendInstant = 1L;
        long subtrahendInstant = 1L;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @org.junit.Test
    public void testGetUnitMillis() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        long unitMillis = unsupported.getUnitMillis();
        assertEquals(0, unitMillis);
    }

    @org.junit.Test
    public void testCompareTo() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField supported = new DurationField(1, DurationFieldType.MILLISECONDS);
        assertEquals(-1, unsupported.compareTo(supported));
    }

    @org.junit.Test
    public void testEquals() {
        DurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField supported = new DurationField(1, DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, supported);
        assertNotEquals(unsupported1, unsupported3);
    }

    @org.junit.Test
    public void testHashCode() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(unsupported.getName().hashCode(), unsupported.hashCode());
    }

    @org.junit.Test
    public void testToString() {
        DurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
    }
}