package org.joda.time.field;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.DurationFieldType;

import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    private UnsupportedDurationField unsupportedDurationField;

    @Before
    public void setUp() {
        unsupportedDurationField = UnsupportedDurationField.getInstance(DurationFieldType.minutes());
    }

    @Test
    public void testGetType() {
        assertEquals(DurationFieldType.minutes(), unsupportedDurationField.getType());
    }

    @Test
    public void testGetName() {
        assertEquals("minutes", unsupportedDurationField.getName());
    }

    @Test
    public void testIsSupported() {
        assertFalse(unsupportedDurationField.isSupported());
    }

    @Test
    public void testIsPrecise() {
        assertTrue(unsupportedDurationField.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        unsupportedDurationField.getValue(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        unsupportedDurationField.getValueAsLong(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        unsupportedDurationField.getValue(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        unsupportedDurationField.getValueAsLong(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        unsupportedDurationField.getMillis(1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLong() {
        unsupportedDurationField.getMillis(1000L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        unsupportedDurationField.getMillis(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueLongAndInstant() {
        unsupportedDurationField.getMillis(1000L, 500L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndValue() {
        unsupportedDurationField.add(1000, 5);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstantAndLongValue() {
        unsupportedDurationField.add(1000, 5L);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceWithInstantValues() {
        unsupportedDurationField.getDifference(1000, 500);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongWithInstantValues() {
        unsupportedDurationField.getDifferenceAsLong(1000, 500);
    }

    @Test
    public void testGetUnitMillis() {
        assertEquals(0, unsupportedDurationField.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, unsupportedDurationField.compareTo(unsupportedDurationField));
    }

    @Test
    public void testEquals() {
        assertTrue(unsupportedDurationField.equals(unsupportedDurationField));
        assertFalse(unsupportedDurationField.equals(null));
        assertFalse(unsupportedDurationField.equals(new Object()));
        assertTrue(unsupportedDurationField.equals(UnsupportedDurationField.getInstance(DurationFieldType.minutes())));
    }

    @Test
    public void testHashCode() {
        assertEquals(unsupportedDurationField.getName().hashCode(), unsupportedDurationField.hashCode());
    }
}