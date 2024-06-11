package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported1, unsupported2);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("seconds", unsupported.getName());
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillisWithValue() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMillisWithValueAndInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1L, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testadd() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testaddWithInstant() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1, 1L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifference() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifference(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifferenceAsLong(1, 1);
    }

    @Test
    public void testgetUnitMillis() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testcompareTo() {
        DurationFieldType type1 = DurationFieldType.seconds();
        DurationFieldType type2 = DurationFieldType.millis();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        DurationFieldType type1 = DurationFieldType.seconds();
        DurationFieldType type2 = DurationFieldType.seconds();
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        UnsupportedDurationField unsupported3 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported4 = new UnsupportedDurationField(null);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported3);
        assertNotEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported4);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String name = unsupported.getName();
        int expectedHashCode = name.hashCode();
        int actualHashCode = unsupported.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String expectedString = "UnsupportedDurationField[seconds]";
        String actualString = unsupported.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.seconds();
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        Object readResolve = unsupported.readResolve();
        assertEquals(UnsupportedDurationField.getInstance(type), readResolve);
    }

}