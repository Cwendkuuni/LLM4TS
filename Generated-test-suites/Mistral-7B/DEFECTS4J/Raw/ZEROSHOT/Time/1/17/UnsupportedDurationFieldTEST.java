package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        field.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 1;
        field.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        field.getValue(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        field.getValueAsLong(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 1;
        field.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        field.getMillis(instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        field.add(instant, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 1;
        field.add(instant, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1;
        long subtrahendInstant = 2;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 1;
        long subtrahendInstant = 2;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long millis = field.getUnitMillis();
        assertEquals(0, millis);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.HOURS();
        DurationFieldType type2 = DurationFieldType.MINUTES();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int hashCode = field.hashCode();
        assertEquals(hashCode, hashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.HOURS();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String toString = field.toString();
        String expected = "UnsupportedDurationField[HOURS]";
        assertEquals(expected, toString);
    }

}