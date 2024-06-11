package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1.getType(), field2.getType());
        assertSame(field1, field2);
    }

    @Test
    public void testName() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("years", field.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAt() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAt(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAt() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLongAt(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAt() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillisAt(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.years();
        DurationFieldType type2 = DurationFieldType.months();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        field1.compareTo(field2);
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.months());
        assertEquals(field1, field1);
        assertEquals(field1, field2);
        assertNotEquals(field1, field3);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(52, field.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.years();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[years]", field.toString());
    }
}