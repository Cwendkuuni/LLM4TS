package org.joda.time.field;

import org.joda.time.DurationFieldType;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY);
        assertSame(field1, field2);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertEquals(DurationFieldType.DAY_OF_MONTH, field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertEquals("dayOfMonth", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getValue(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getValueAsLong(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getValue(10, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getValueAsLong(10, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getMillis(10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getMillis(10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getMillis(10, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAsLongAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getMillis(10L, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.add(1000, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.add(1000L, 10L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getDifference(5000, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.getDifferenceAsLong(5000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY);
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(10));
        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(field2));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOUR_OF_DAY);
        assertNotEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertEquals("UnsupportedDurationField[dayOfMonth]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        assertSame(field, field.readResolve());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAY_OF_MONTH);
        field.unsupported();
    }
}