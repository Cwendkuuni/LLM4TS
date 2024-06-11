package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetType() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(DurationFieldType.weeks(), field.getType());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(DurationFieldType.weeks().getName(), field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValue(10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValueAsLong(10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValue(10000L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getValueAsLong(10000L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(1, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getMillis(1L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndIntValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.add(10000L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.add(10000L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getDifference(10000L, 10000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        field.getDifferenceAsLong(10000L, 10000L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertTrue(field1.hashCode() == field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertEquals("UnsupportedDurationField[weeks]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.weeks());
        assertSame(field1, field2.readResolve());
    }

}