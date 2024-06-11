package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        assertEquals(DurationFieldType.seconds(), durationField.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertEquals("minutes", durationField.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertFalse(durationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(durationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        durationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        durationField.getValueAsLong(60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        durationField.getValue(30, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        durationField.getValueAsLong(12, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
        durationField.getMillis(365);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        durationField.getMillis(60);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        durationField.getMillis(30, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        durationField.getMillis(12, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.millis());
        durationField.add(1000, 100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        durationField.add(60, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        durationField.getDifference(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.days());
        durationField.getDifferenceAsLong(86400000, 43200000);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField durationField = UnsupportedDurationField.getInstance(DurationFieldType.hours());
        assertEquals(0, durationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField durationField1 = UnsupportedDurationField.getInstance(DurationFieldType.seconds());
        UnsupportedDurationField durationField2 = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
        assertTrue(durationField1.compareTo(durationField2) > 0);
        assertTrue(durationField2.compareTo(durationField1) < 0);
    }

}