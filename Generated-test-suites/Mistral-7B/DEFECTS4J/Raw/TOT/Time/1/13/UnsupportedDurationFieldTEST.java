package org.joda.time.field.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DurationFieldType;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests the getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTES();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField anotherField = UnsupportedDurationField.getInstance(type);
        assertEquals("Unexpected field instance", field, anotherField);
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertFalse("Unsupported field should not be supported", field.isSupported());
    }

    /**
     * Tests the getName method.
     */
    @Test
    public void testGetName() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertEquals("Unexpected field name", "Minutes", field.getName());
    }

    /**
     * Tests the getValue method with long duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongDuration() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long duration = 1234567890L;
        field.getValue(duration);
    }

    /**
     * Tests the getValue method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long instant = 1234567890L;
        field.getValue(instant);
    }

    /**
     * Tests the getValueAsLong method with long duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongDuration() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long duration = 1234567890L;
        field.getValueAsLong(duration);
    }

    /**
     * Tests the getValueAsLong method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long instant = 1234567890L;
        field.getValueAsLong(instant);
    }

    /**
     * Tests the getValue method with int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        int value = 123;
        field.getValue(value);
    }

    /**
     * Tests the getValueAsLong method with int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        int value = 123;
        field.getValueAsLong(value);
    }

    /**
     * Tests the getMillis method with int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        int value = 123;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long value = 1234567890L;
        field.getMillis(value);
    }

    /**
     * Tests the getMillis method with int value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntValueInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        int value = 123;
        long instant = 1234567890L;
        field.getMillis(value, instant);
    }

    /**
     * Tests the getMillis method with long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongValueInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long value = 1234567890L;
        long instant = 1234567890L;
        field.getMillis(value, instant);
    }

    /**
     * Tests the add method with instant and int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstantIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long instant = 1234567890L;
        int value = 123;
        field.add(instant, value);
    }

    /**
     * Tests the add method with instant and long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddInstantLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long instant = 1234567890L;
        long value = 1234567890L;
        field.add(instant, value);
    }

    /**
     * Tests the getDifference method with long minuend instant and long subtrahend instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongMinuendInstantLongSubtrahendInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567890L;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests the getDifference method with long minuend instant and int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongMinuendInstantIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        int value = 123;
        field.getDifference(minuendInstant, value);
    }

    /**
     * Tests the getDifference method with long minuend instant and long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongMinuendInstantLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        long value = 1234567890L;
        field.getDifference(minuendInstant, value);
    }

    /**
     * Tests the getDifferenceAsLong method with long minuend instant and long subtrahend instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLongMinuendInstantLongSubtrahendInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        long subtrahendInstant = 1234567890L;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Tests the getDifferenceAsLong method with long minuend instant and int value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLongMinuendInstantIntValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        int value = 123;
        field.getDifferenceAsLong(minuendInstant, value);
    }

    /**
     * Tests the getDifferenceAsLong method with long minuend instant and long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLongMinuendInstantLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long minuendInstant = 1234567890L;
        long value = 1234567890L;
        field.getDifferenceAsLong(minuendInstant, value);
    }

    /**
     * Tests the getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        long result = field.getUnitMillis();
        assertEquals("Unexpected unit millis", 0, result);
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        DurationField field2 = new DurationField(DurationFieldType.HOURS()) {
            @Override
            public int getDifference(long minuendInstant, long subtrahendInstant) {
                return 1;
            }
        };
        int result = field1.compareTo(field2);
        assertTrue("Unexpected comparison result", result < 0);
    }

    /**
     * Tests the equals method with null object.
     */
    @Test
    public void testEqualsNullObject() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertFalse("Unexpected equals result", field.equals(null));
    }

    /**
     * Tests the equals method with different object.
     */
    @Test
    public void testEqualsDifferentObject() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS());
        assertFalse("Unexpected equals result", field1.equals(field2));
    }

    /**
     * Tests the equals method with same object.
     */
    @Test
    public void testEqualsSameObject() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertTrue("Unexpected equals result", field.equals(field));
    }
}