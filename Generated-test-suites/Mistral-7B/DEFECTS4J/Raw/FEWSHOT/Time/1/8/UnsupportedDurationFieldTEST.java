package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.joda.time.DurationFieldType;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertSame(field1, field2);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("days", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue(long duration) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValue(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong(long duration) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getValueAsLong(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant) {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getDifference(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        field.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        int compare = field1.compareTo(field2);
        assertEquals(1, compare);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashCode = field.hashCode();
        assertEquals("days".hashCode(), hashCode);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[days]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.DAYS), field);
    }
}