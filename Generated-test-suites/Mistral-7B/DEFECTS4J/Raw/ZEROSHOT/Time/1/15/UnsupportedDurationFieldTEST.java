package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.joda.time.field.UnsupportedDurationField;

import org.junit.Test;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests caching of UnsupportedDurationField instances.
     */
    @Test
    public void testCaching() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    /**
     * Tests the name of an UnsupportedDurationField.
     */
    @Test
    public void testName() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("milliseconds", field.getName());
    }

    /**
     * Tests the isSupported method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    /**
     * Tests the isPrecise method.
     */
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    /**
     * Tests the getValue method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(0);
    }

    /**
     * Tests the getValueAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0);
    }

    /**
     * Tests the getValue method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(0, 0);
    }

    /**
     * Tests the getValueAsLong method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(0, 0);
    }

    /**
     * Tests the getMillis method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(0);
    }

    /**
     * Tests the getMillis method with a long value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValue() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1);
    }

    /**
     * Tests the getMillis method with a int value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithIntValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1, 0);
    }

    /**
     * Tests the getMillis method with a long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1L, 0);
    }

    /**
     * Tests the add method with instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 1);
    }

    /**
     * Tests the add method with a long value and instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithLongValueAndInstant() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(0, 1L);
    }

    /**
     * Tests the getDifference method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(0, 0);
    }

    /**
     * Tests the getDifferenceAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(0, 0);
    }

    /**
     * Tests the getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MILLISECONDS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(1, field1.compareTo(field2));
        assertSame(field1, field1);
        assertSame(field2, field2);
    }

}