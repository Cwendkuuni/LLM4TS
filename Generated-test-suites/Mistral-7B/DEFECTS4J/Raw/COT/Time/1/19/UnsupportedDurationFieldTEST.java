package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Unit tests for the UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, another);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        boolean isSupported = field.isSupported();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetValueInstead() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetValueAsLongInstead() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetMillisLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.add(0, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedAddLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.add(0, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        field.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        long unitMillis = field.getUnitMillis();
        assertEquals(0, unitMillis);
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.INSTANT);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        int comparison = field1.compareTo(field2);
        assertEquals(1, comparison);
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.INSTANT);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.INSTANT);
        boolean equals = field1.equals(field2);
        assertTrue(equals);

        Object obj = new Object();
        boolean notEquals = field1.equals(obj);
        assertFalse(notEquals);

        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        boolean notEquals2 = field1.equals(field3);
        assertFalse(notEquals2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        int hashCode = field.hashCode();
        DurationFieldType type = field.getType();
        String name = type.getName();
        int nameHashCode = name.hashCode();
        assertEquals(hashCode, nameHashCode);
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        String toString = field.toString();
        String expected = "UnsupportedDurationField[INSTANT]";
        assertEquals(expected, toString);
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.INSTANT);
        UnsupportedDurationField resolved = (UnsupportedDurationField) field.readResolve();
        assertEquals(field, resolved);
    }

}