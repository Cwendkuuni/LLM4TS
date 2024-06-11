package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertSame(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValue(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getMillis(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.add(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifference(0L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        field.getDifferenceAsLong(0L, 0L);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(field1, field2);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        int hashCode = field.hashCode();
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals(hashCode, field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }
}