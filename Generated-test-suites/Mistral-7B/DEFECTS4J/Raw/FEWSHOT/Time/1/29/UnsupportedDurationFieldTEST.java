package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertFalse(unsupported.isSupported());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_2() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getValueAsLong(1L, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_2() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_3() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getMillis(1, 0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_2() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getDifference(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupported.getDifferenceAsLong(0L, 1L);
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(DurationFieldType.MINUTES, unsupported.getType());
    }

    @Test
    public void testgetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals("minutes", unsupported.getName());
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertNotSame(this, unsupported);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(unsupported, unsupported2);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertNotEquals(unsupported, unsupported3);
        Object obj = new Object();
        assertNotEquals(unsupported, obj);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        int hashCode = unsupported.hashCode();
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals(hashCode, unsupported2.hashCode());
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(DurationFieldType.SECONDS);
        assertNotEquals(hashCode, unsupported3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MINUTES);
        assertEquals("UnsupportedDurationField[minutes]", unsupported.toString());
    }
}