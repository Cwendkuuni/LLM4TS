package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationField field1 = UnsupportedDurationField.getInstance(type1);

        DurationFieldType type2 = DurationFieldType.MINUTES;
        DurationField field2 = UnsupportedDurationField.getInstance(type2);

        assertNotSame(field1, field2);
        assertEquals(type1, field1.getType());
        assertEquals(type2, field2.getType());
    }

    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue(field.isPrecise());
    }

    @Test
    public void testValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(123456L));
    }

    @Test
    public void testValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(123456L));
    }

    @Test
    public void testValueAtInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getValue(123456L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> field.getValueAsLong(123456L, 0L));
    }

    @Test
    public void testGetMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(123456L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(1, 0L));
        assertThrows(UnsupportedOperationException.class, () -> field.getMillis(123456L, 0L));
    }

    @Test
    public void testAdd() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.add(0L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(123456L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.add(0L, 1, 0L));
        assertThrows(UnsupportedOperationException.class, () -> field.add(123456L, 1, 0L));
    }

    @Test
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(0L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(0L, 1));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifference(123456L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> field.getDifferenceAsLong(123456L, 0L));
    }

    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(0, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertEquals(1, field1.compareTo(field2));
    }

    @Test
    public void testEquals() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        DurationField field3 = new DurationField(DurationFieldType.DAYS) {
            @Override
            public boolean isSupported() {
                return true;
            }
        };
        assertTrue(field1.equals(field1));
        assertTrue(field1.equals(field2));
        assertFalse(field1.equals(field3));
    }

    @Test
    public void testHashCode() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field.hashCode(), field.getType().getName().hashCode());
    }

    @Test
    public void testToString() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("UnsupportedDurationField[DAYS]", field.toString());
    }

    @Test
    public void testReadResolve() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(field.getType(), UnsupportedDurationField.getInstance(field.getType()).getType());
    }
}