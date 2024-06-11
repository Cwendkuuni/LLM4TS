package org.joda.time.field.test;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.field.UnsupportedDurationField.getInstance;

/**
 * Test class for UnsupportedDurationField
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = getInstance(type);
        UnsupportedDurationField other = getInstance(type);
        assertEquals(unsupported, other);
    }

    /**
     * Test isSupported method
     */
    @Test
    public void testIsSupported() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        assertFalse(unsupported.isSupported());
    }

    /**
     * Test isPrecise method
     */
    @Test
    public void testIsPrecise() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        assertTrue(unsupported.isPrecise());
    }

    /**
     * Test getValue method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getValue(1);
    }

    /**
     * Test getValueAsLong method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getValueAsLong(1);
    }

    /**
     * Test getValue method with instant
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getValue(1, 0L);
    }

    /**
     * Test getValueAsLong method with instant
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getValueAsLong(1, 0L);
    }

    /**
     * Test getMillis method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getMillis(1);
    }

    /**
     * Test getMillis method with value
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getMillis(1, 0L);
    }

    /**
     * Test getMillis method with value and instant
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getMillis(1, 0L);
    }

    /**
     * Test add method with instant
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithInstant() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.add(1, 0L);
    }

    /**
     * Test add method with value and instant
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithValueAndInstant() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.add(1, 0L, 0L);
    }

    /**
     * Test getDifference method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getDifference(1, 1);
    }

    /**
     * Test getDifferenceAsLong method
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        unsupported.getDifferenceAsLong(1, 1);
    }

    /**
     * Test getUnitMillis method
     */
    @Test
    public void testGetUnitMillis() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        assertEquals(0, unsupported.getUnitMillis());
    }

    /**
     * Test compareTo method
     */
    @Test
    public void testCompareTo() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        DurationField supported = new ConstantDurationField(DurationFieldType.SECONDS(), 1);
        assertTrue(supported.compareTo(unsupported) < 0);
    }

    /**
     * Test equals method with null object
     */
    @Test
    public void testEqualsWithNullObject() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        assertFalse(unsupported.equals(null));
    }

    /**
     * Test equals method with another UnsupportedDurationField instance
     */
    @Test
    public void testEqualsWithAnotherUnsupportedDurationField() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported1 = getInstance(type);
        UnsupportedDurationField unsupported2 = getInstance(type);
        assertEquals(unsupported1, unsupported2);
    }

    /**
     * Test equals method with another DurationField instance
     */
    @Test
    public void testEqualsWithAnotherDurationField() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        DurationField supported = new ConstantDurationField(DurationFieldType.SECONDS(), 1);
        assertFalse(unsupported.equals(supported));
    }

    /**
     * Test hashCode method
     */
    @Test
    public void testHashCode() {
        DurationField unsupported = getInstance(DurationFieldType.SECONDS());
        String name = unsupported.getName();
        DurationFieldType type = unsupported.getType();
        int hashCode = unsupported.hashCode();
        DurationField unsupported2 = getInstance(type);
        assertEquals(hashCode, unsupported2.hashCode());
        assertEquals(hashCode, name.hashCode());
    }
}