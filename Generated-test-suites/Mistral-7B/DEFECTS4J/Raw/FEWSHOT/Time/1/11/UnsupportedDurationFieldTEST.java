package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("Unsupported", unsupported.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1L));
    }

    @Test
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1L));
    }

    @Test
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1L, 0L));
    }

    @Test
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1L));
    }

    @Test
    public void testGetMillisWithInstant() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(0L, 0L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1L, 0L));
    }

    @Test
    public void testAdd() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(0L, 1));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1L, 1));
    }

    @Test
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(0L, 1L));
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(0L, 1L));
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.getInstance("SECONDS");
        DurationFieldType type2 = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.getInstance("SECONDS");
        DurationFieldType type2 = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type1);
        assertTrue(unsupported1.equals(unsupported1));
        assertTrue(unsupported1.equals(unsupported3));
        assertFalse(unsupported1.equals(new Object()));
        assertFalse(unsupported1.equals(unsupported2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported.hashCode(), unsupported.hashCode());
        assertEquals(unsupported.hashCode(), new UnsupportedDurationField(type).hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[Unsupported]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertSame(UnsupportedDurationField.getInstance(type), unsupported.readResolve());
    }

}