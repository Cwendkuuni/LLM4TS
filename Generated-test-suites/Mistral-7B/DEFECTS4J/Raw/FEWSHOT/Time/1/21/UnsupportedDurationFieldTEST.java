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
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertNotNull(unsupported);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(unsupported1.equals(unsupported2));

        UnsupportedDurationField unsupported1_1 = UnsupportedDurationField.getInstance(type1);
        assertTrue(unsupported1.equals(unsupported1_1));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int hashCode = unsupported.hashCode();
        DurationFieldType type_1 = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported_1 = UnsupportedDurationField.getInstance(type_1);
        assertEquals(hashCode, unsupported_1.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[DAYS]", unsupported.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(0L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue(long duration) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong(long duration) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue(long duration, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(1234567890123L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong(long duration, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1234567890123L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(3L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(int value, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(3, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis(long value, long instant) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(3L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1234567890123L, 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, int value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1234567890123L, 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd(long instant, long value) {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1234567890123L, 3L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifference(1234567890123L, 1234567890123L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifferenceAsLong(1234567890123L, 1234567890123L);
    }

    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.DAYS;
        DurationFieldType type2 = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

}