package org.joda.time.field.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedDurationField;

/**
 * Tests for the {@link UnsupportedDurationField} class.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getting an instance of UnsupportedDurationField for a specific named field.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.DAYS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("Unexpected name", type.getName(), field.getName());
    }

    /**
     * Test getting an instance of UnsupportedDurationField for an unsupported type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test getting an instance of UnsupportedDurationField for an unknown type.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetInstanceWithUnknownType() {
        DurationFieldType unknownType = new DurationFieldType("TEST-TYPE") {
            @Override
            public String getName() {
                return "Test Type";
            }
        };
        UnsupportedDurationField.getInstance(unknownType);
    }

    /**
     * Test the isSupported method.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertFalse("Supported field should not be supported", unsupportedField.isSupported());
    }

    /**
     * Test the isPrecise method.
     */
    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue("Unsupported field should be precise", unsupportedField.isPrecise());
    }

    /**
     * Test the getValue method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(0);
    }

    /**
     * Test the getValueAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(0);
    }

    /**
     * Test the getValue method with an instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValue(1, System.currentTimeMillis());
    }

    /**
     * Test the getValueAsLong method with an instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongWithInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getValueAsLong(1, System.currentTimeMillis());
    }

    /**
     * Test the getMillis method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(0);
    }

    /**
     * Test the getMillis method with a value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValue() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1);
    }

    /**
     * Test the getMillis method with a value and an instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1, System.currentTimeMillis());
    }

    /**
     * Test the getMillis method with a value and an instant.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisWithValueAndInstant2() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getMillis(1, System.currentTimeMillis() + 1000L);
    }

    /**
     * Test the add method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(System.currentTimeMillis(), 1);
    }

    /**
     * Test the add method with a duration.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddWithDuration() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.add(1, DurationFieldType.MILLISECONDS, 1);
    }

    /**
     * Test the getDifference method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifference(System.currentTimeMillis() - 1000, System.currentTimeMillis());
    }

    /**
     * Test the getDifferenceAsLong method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        unsupportedField.getDifferenceAsLong(System.currentTimeMillis() - 1000, System.currentTimeMillis());
    }

    /**
     * Test the getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("Unexpected unit millis", 0, unsupportedField.getUnitMillis());
    }

    /**
     * Test the compareTo method.
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.HOURS);
        assertTrue("Unsupported fields should compare as unequal", unsupportedField1.compareTo(unsupportedField2) != 0);
    }

    /**
     * Test the equals method.
     */
    @Test
    public void testEquals() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertTrue("Unexpected equals result", unsupportedField1.equals(unsupportedField2));
    }

    /**
     * Test the hashCode method.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        assertEquals("Unexpected hash codes", unsupportedField1.hashCode(), unsupportedField2.hashCode());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType.DAYS);
        String toString = unsupportedField.toString();
        assertTrue("Unexpected toString result", toString.startsWith("UnsupportedDurationField["));
    }

    /**
     * Test the readResolve method.
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.DAYS);
        UnsupportedDurationField resolvedField = (UnsupportedDurationField) unsupportedField.readResolve();
        assertEquals("Unexpected name", DurationFieldType.DAYS, resolvedField.getType());
    }
}