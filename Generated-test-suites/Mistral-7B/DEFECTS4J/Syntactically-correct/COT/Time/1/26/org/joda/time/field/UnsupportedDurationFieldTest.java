package org.joda.time.field.test;

import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;

import org.joda.time.field.UnsupportedDurationField;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField
 */
public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, unsupported2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        unsupported.getValue(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        unsupported.getValueAsLong(duration);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_2args() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long duration = 10;
        long instant = 12345;
        unsupported.getValueAsLong(duration, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int value = 10;
        unsupported.getMillis(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_2args() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long value = 10;
        long instant = 12345;
        unsupported.getMillis(value, instant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 10;
        long value = 5;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_2args() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long instant = 10;
        long value = 5;
        unsupported.add(instant, value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 10;
        long subtrahendInstant = 5;
        unsupported.getDifference(minuendInstant, subtrahendInstant);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 10;
        long subtrahendInstant = 5;
        unsupported.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    @Test
    public void testCompareTo() {
        DurationFieldType supported = DurationFieldType.SECONDS();
        DurationFieldType unsupported = DurationFieldType.milliseconds();
        UnsupportedDurationField unsupportedDurationField = UnsupportedDurationField.getInstance(unsupported);
        assertTrue(unsupportedDurationField.compareTo(supported) > 0);
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported1.equals(unsupported2));
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int hashCode = unsupported.hashCode();
        assertEquals(hashCode, unsupported.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String toString = unsupported.toString();
        assertTrue(toString.contains("UnsupportedDurationField"));
    }

    @Test
    public void testSingletonSerialization() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        byte[] byteArray = null;
        try {
            byteArray = java.io.ObjectOutputStream.writeObject(new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream()));
            java.io.ObjectInputObjectInput input = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(byteArray));
            UnsupportedDurationField unsupported2 = (UnsupportedDurationField) input.readObject();
            assertEquals(unsupported, unsupported2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}