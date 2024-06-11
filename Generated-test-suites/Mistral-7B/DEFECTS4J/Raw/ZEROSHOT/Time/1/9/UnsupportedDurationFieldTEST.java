package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
        assertFalse(unsupported.isSupported());
        assertTrue(unsupported.isPrecise());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123;
        unsupported.getValue(duration);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123;
        unsupported.getValueAsLong(duration);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123;
        long instant = 456;
        unsupported.getValue(duration, instant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 123;
        long instant = 456;
        unsupported.getValueAsLong(duration, instant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 123;
        unsupported.getMillis(value);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 123;
        unsupported.getMillis(value);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 123;
        long instant = 456;
        unsupported.getMillis(value, instant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 123;
        long instant = 456;
        unsupported.getMillis(value, instant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, int)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInt() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 123;
        int value = 456;
        unsupported.add(instant, value);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 123;
        long value = 456;
        unsupported.add(instant, value);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifference(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 123;
        long subtrahendInstant = 456;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifferenceAsLong(long, long)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 123;
        long subtrahendInstant = 456;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getUnitMillis()}.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    /**
     * Test method for {@link UnsupportedDurationField#compareTo(DurationField)}.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.SECONDS();
        DurationFieldType type2 = DurationFieldType.MILLISECONDS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }
}