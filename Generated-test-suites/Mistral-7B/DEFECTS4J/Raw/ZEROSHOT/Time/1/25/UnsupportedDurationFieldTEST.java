package org.joda.time.field;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertFalse(unsupportedField.isSupported());
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals("hours", unsupportedField.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getValueAsLong(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getDifference(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        unsupportedField.getDifferenceAsLong(1L, 1L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        assertEquals(0, unsupportedField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        int result = unsupportedField1.compareTo(unsupportedField2);
        assertEquals(1, result);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        UnsupportedDurationField unsupportedField3 = new UnsupportedDurationField(DurationFieldType.MINUTES());
        assertTrue(unsupportedField1.equals(unsupportedField1));
        assertTrue(unsupportedField1.equals(unsupportedField2));
        assertFalse(unsupportedField1.equals(unsupportedField3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        int hashCode = unsupportedField.hashCode();
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(DurationFieldType.HOURS());
        int hashCode2 = unsupportedField2.hashCode();
        assertEquals(hashCode, hashCode2);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.HOURS());
        String toString = unsupportedField.toString();
        String expected = "UnsupportedDurationField[hours]";
        assertEquals(expected, toString);
    }
}