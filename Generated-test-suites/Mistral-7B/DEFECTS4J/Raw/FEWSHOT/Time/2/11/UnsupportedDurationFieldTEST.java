package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(field.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        field.getValue(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        field.getValue(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongAtInstant() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        field.getValueAsLong(1L, 1L);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals("milliseconds", field.getName());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(field1, field2);

        Object other = new Object();
        assertFalse(field1.equals(other));

        UnsupportedDurationField field3 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(field1.hashCode(), field2.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[milliseconds]", field.toString());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, field1.compareTo(field2));
    }
}