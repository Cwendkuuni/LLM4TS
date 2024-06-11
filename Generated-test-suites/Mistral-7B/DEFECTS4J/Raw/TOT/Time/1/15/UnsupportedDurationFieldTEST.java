package org.joda.time.field.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for UnsupportedDurationField class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        assertEquals(DurationFieldType.SECONDS_30, field.getType());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        assertEquals("seconds-30", field.getName());
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        assertFalse(field.isSupported());
    }

    /**
     * Test isPrecise method.
     */
    @Test
    public void testIsPrecise() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        assertTrue(field.isPrecise());
    }

    /**
     * Test getValue method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getValue(1);
    }

    /**
     * Test getValueAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getValueAsLong(1);
    }

    /**
     * Test getValue method with long duration and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithLongDurationAndLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getValue(1, 1234567890123L);
    }

    /**
     * Test getValueAsLong method with long duration and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithLongDurationAndLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getValueAsLong(1, 1234567890123L);
    }

    /**
     * Test getMillis method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getMillis(1);
    }

    /**
     * Test getMillis method with long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getMillis(1234567890123L);
    }

    /**
     * Test getMillis method with int value and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getMillis(1, 1234567890123L);
    }

    /**
     * Test getMillis method with long value and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getMillis(1234567890123L, 1234567890123L);
    }

    /**
     * Test add method with long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.add(1234567890123L, 1);
    }

    /**
     * Test add method with int value and long instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithIntValueAndLongInstant() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.add(1234567890123L, 1);
    }

    /**
     * Test getDifference method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getDifference(1, 1);
    }

    /**
     * Test getDifferenceAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        field.getDifferenceAsLong(1, 1);
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationField field = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationField field1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS_30);
        DurationField field2 = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES_1);
        assertEquals(1, field1.compareTo(field2));
    }
}