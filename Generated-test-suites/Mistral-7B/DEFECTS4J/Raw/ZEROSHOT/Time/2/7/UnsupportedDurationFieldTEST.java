package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
        assertEquals(0, unsupported.getUnitMillis());
        assertEquals(0, unsupported.compareTo(null));
        assertTrue(unsupported.isPrecise());
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetInstanceNullType() {
        assertNull(UnsupportedDurationField.getInstance(null));
    }

    @Test
    public void testGetInstanceTwice() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported1, unsupported2);
    }

    @Test
    public void testHashCodeAndEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(unsupported1.hashCode(), unsupported1.hashCode());
        assertFalse(unsupported1.equals(unsupported2));
        assertFalse(unsupported1.equals(null));
        assertFalse(unsupported1.equals("test"));
    }

    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(0L));
    }

    @Test
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(0L));
    }

    @Test
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(0L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(0L, 0L));
    }

    @Test
    public void testMilliseconds() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(0L, 0));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(0L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(0L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(0L, 0L));
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, unsupported1.compareTo(unsupported2));
    }
}