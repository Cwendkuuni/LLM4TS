package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.joda.time.field.UnsupportedDurationField;
import static org.junit.Assert.*;
import org.junit.Test;

public class UnsupportedDurationFieldTest {
    @Test
    public void testGetType() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertEquals(DurationFieldType.MINUTE_OF_HOUR, durationField.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertEquals("minuteOfHour", durationField.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertFalse(durationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertTrue(durationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getValue(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getValueAsLong(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getValue(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getValueAsLong(100, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getMillis(100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getMillis(100L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getMillis(100, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithInstantAndValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getMillis(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.add(100L, 200);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.add(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getDifference(100L, 200L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        durationField.getDifferenceAsLong(100L, 200L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertEquals(0, durationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField durationField1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        UnsupportedDurationField durationField2 = UnsupportedDurationField.getInstance(DurationFieldType.SECOND_OF_MINUTE);
        assertEquals(1, durationField1.compareTo(durationField2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField durationField1 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        UnsupportedDurationField durationField2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertTrue(durationField1.equals(durationField2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertEquals("minuteOfHour".hashCode(), durationField.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.MINUTE_OF_HOUR);
        assertEquals("UnsupportedDurationField[minuteOfHour]", durationField.toString());
    }
}