package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testInstance() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(DurationFieldType.MILLISECONDS, field.getType());
        assertFalse(field.isSupported());
    }

    @Test
    public void testCache() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertSame(field1, field2);
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("Milliseconds", field.getName());
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testGetValue() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1));
    }

    @Test
    public void testGetValueAtInstant() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(1, 0));
    }

    @Test
    public void testGetMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 0));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1L, 0));
    }

    @Test
    public void testAdd() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(1, 1));
    }

    @Test
    public void testGetDifference() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0, 1));
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(0, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField field3 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
        assertFalse(field1.equals(null));
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals(67108867, field.hashCode()); // Milliseconds
    }

    @Test
    public void testToString() {
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[Milliseconds]", field.toString());
    }

    @Test
    public void testReadResolve() {
        UnsupportedDurationField field = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertSame(UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS), field.readResolve());
    }
}