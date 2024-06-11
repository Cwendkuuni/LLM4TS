package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testValues() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertNull(field.getValue(0));
        assertNull(field.getValueAsLong(0));
        assertNull(field.getValue(0, 1L));
        assertNull(field.getValueAsLong(0, 1L));
        assertNull(field.getMillis(0));
        assertNull(field.getMillis(0L));
        assertNull(field.getMillis(0, 1L));
        assertNull(field.getMillis(0L, 1L));
        assertNull(field.add(0, 1));
        assertNull(field.add(0, 1L));
        assertNull(field.getDifference(0, 0));
        assertNull(field.getDifferenceAsLong(0, 0));
        assertEquals(0, field.getUnitMillis());
        assertEquals(0, field.compareTo(null));

        assertNull(field.getValue(Long.MAX_VALUE));
        assertNull(field.getValueAsLong(Long.MAX_VALUE));
        assertNull(field.getValue(Long.MIN_VALUE));
        assertNull(field.getValueAsLong(Long.MIN_VALUE));
        assertNull(field.getMillis(Integer.MAX_VALUE));
        assertNull(field.getMillis(Long.MAX_VALUE));
        assertNull(field.getMillis(Integer.MIN_VALUE));
        assertNull(field.getMillis(Long.MIN_VALUE));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field1, field2);

        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertNotSame(field1, field3);

        Object obj = new Object();
        assertNotSame(field1, obj);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        int hashCode = field.hashCode();
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(hashCode, field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String toString = field.toString();
        String expected = "UnsupportedDurationField[DAYS]";
        assertEquals(expected, toString);
    }

    @Test
    public void testResolve() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertNotNull(field.readResolve());
    }
}