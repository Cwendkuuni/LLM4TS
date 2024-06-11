package org.joda.time.field.test;

import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import org.junit.Test;

/**
 * Test cases for {@link UnsupportedDurationField} class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assert (unsupported != null);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    /**
     * Test getValue method with long duration.
     */
    @Test
    public void testGetValueLongDuration() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(duration));
    }

    /**
     * Test getValue method with long instant.
     */
    @Test
    public void testGetValueLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(instant));
    }

    /**
     * Test getValueAsLong method with long duration.
     */
    @Test
    public void testGetValueAsLongLongDuration() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(duration));
    }

    /**
     * Test getValueAsLong method with long instant.
     */
    @Test
    public void testGetValueAsLongLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(instant));
    }

    /**
     * Test getValue method with long duration and long instant.
     */
    @Test
    public void testGetValueLongDurationLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(duration, instant));
    }

    /**
     * Test getValueAsLong method with long duration and long instant.
     */
    @Test
    public void testGetValueAsLongLongDurationLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 1000;
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(duration, instant));
    }

    /**
     * Test getMillis method with int value.
     */
    @Test
    public void testGetMillisIntValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(value));
    }

    /**
     * Test getMillis method with long value.
     */
    @Test
    public void testGetMillisLongValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1000;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(value));
    }

    /**
     * Test getMillis method with int value and long instant.
     */
    @Test
    public void testGetMillisIntValueLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 1;
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(value, instant));
    }

    /**
     * Test getMillis method with long value and long instant.
     */
    @Test
    public void testGetMillisLongValueLongInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 1000;
        long instant = 1234567890123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(value, instant));
    }

    /**
     * Test add method with long instant and int value.
     */
    @Test
    public void testAddLongInstantIntValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        int value = 1;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, value));
    }

    /**
     * Test add method with long instant and long value.
     */
    @Test
    public void testAddLongInstantLongValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 1234567890123L;
        long value = 1000;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(instant, value));
    }

    /**
     * Test getDifference method with long minuendInstant and long subtrahendInstant.
     */
    @Test
    public void testGetDifferenceLongMinuendInstantLongSubtrahendInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567880123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(minuendInstant, subtrahendInstant));
    }

    /**
     * Test getDifferenceAsLong method with long minuendInstant and long subtrahendInstant.
     */
    @Test
    public void testGetDifferenceAsLongLongMinuendInstantLongSubtrahendInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1234567890123L;
        long subtrahendInstant = 1234567880123L;
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant));
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.seconds();
        DurationFieldType type2 = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }
}