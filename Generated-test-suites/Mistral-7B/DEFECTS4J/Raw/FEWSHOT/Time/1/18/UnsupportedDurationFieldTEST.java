package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported1.toString());
        assertSame(unsupported1, unsupported2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.isSupported();
    }

    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("DAYS", unsupported.getName());
    }

    @Test
    public void testGetType() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals(DurationFieldType.DAYS, unsupported.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong_1() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong_2() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getValueAsLong(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis_1() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis_2() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getMillis(1L, 2L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.add(0L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd_1() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.add(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd_2() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.add(1L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getDifference(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported.getDifferenceAsLong(0L, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testcompareTo() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupported1.compareTo(unsupported2);
    }

    @Test
    public void testequals() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
    }

    @Test
    public void testHashcode() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);

        assertEquals(unsupported1.hashCode(), unsupported1.hashCode());
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
        assertNotEquals(unsupported1.hashCode(), unsupported3.hashCode());
    }
}