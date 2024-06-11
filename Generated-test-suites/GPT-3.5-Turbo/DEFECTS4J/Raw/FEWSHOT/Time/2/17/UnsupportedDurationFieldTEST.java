package org.joda.time.field;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField field;

    @Before
    public void setUp() {
        field = UnsupportedDurationField.getInstance(DurationFieldType.MILLIS_TYPE);
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.MILLIS_TYPE, field.getType());
    }

    @Test
    public void testGetName() {
        assertNull(field.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(field.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(field.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        field.getValue(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        field.getValueAsLong(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        field.getValue(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        field.getValueAsLong(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        field.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        field.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int_long() {
        field.getMillis(1, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        field.getMillis(1000L, 2000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_int() {
        field.add(1000L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        field.add(1000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        field.getDifference(2000L, 1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        field.getDifferenceAsLong(2000L, 1000L);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0L, field.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, field.compareTo(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_TYPE)));
    }

    @Test
    public void testEquals() {
        assertTrue(field.equals(field));
        assertFalse(field.equals(null));
        assertFalse(field.equals(""));
        assertFalse(field.equals(UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_TYPE)));
    }

    @Test
    public void testHashCode() {
        assertTrue(field.hashCode() == field.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("UnsupportedDurationField[null]", field.toString());
    }

    @Test
    public void testReadResolve() {
        assertEquals(field, field.readResolve());
    }

}