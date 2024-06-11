package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DurationFieldType;

import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLISECONDS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported, another);
    }

    /**
     * Test getInstance method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test isSupported method.
     */
    @Test
    public void testIsSupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported.isSupported());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals("Milliseconds", unsupported.getName());
    }

    /**
     * Test getValue method with long argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValue(1L);
    }

    /**
     * Test getValue method with int argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueInt() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValue(1);
    }

    /**
     * Test getValueAsLong method with long argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValueAsLong(1L);
    }

    /**
     * Test getValueAsLong method with int argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongInt() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValueAsLong(1);
    }

    /**
     * Test getValue method with long and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValue(1L, 2L);
    }

    /**
     * Test getValue method with int and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueIntLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getValue(1, 2L);
    }

    /**
     * Test getMillis method with int argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getMillis(1);
    }

    /**
     * Test getMillis method with long argument.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getMillis(1L);
    }

    /**
     * Test getMillis method with int and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getMillis(1, 2L);
    }

    /**
     * Test getMillis method with long and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getMillis(1L, 2L);
    }

    /**
     * Test add method with long and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.add(1L, 2L);
    }

    /**
     * Test add method with int and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIntLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.add(1, 2L);
    }

    /**
     * Test getDifference method with long and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getDifference(1L, 2L);
    }

    /**
     * Test getDifference method with int and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceIntLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getDifference(1, 2L);
    }

    /**
     * Test getDifferenceAsLong method with long and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getDifferenceAsLong(1L, 2L);
    }

    /**
     * Test getDifferenceAsLong method with int and long arguments.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongIntLong() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        unsupported.getDifferenceAsLong(1, 2L);
    }

    /**
     * Test getUnitMillis method.
     */
    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupported.getUnitMillis());
    }

    /**
     * Test compareTo method.
     */
    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(0, unsupported.compareTo(unsupported));
    }

    /**
     * Test equals method with null argument.
     */
    @Test
    public void testEqualsNull() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported.equals(null));
    }

    /**
     * Test equals method with different class.
     */
    @Test
    public void testEqualsDifferentClass() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported.equals(new Object()));
    }

    /**
     * Test equals method with same instance.
     */
    @Test
    public void testEqualsSameInstance() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals(unsupported, unsupported);
    }

    /**
     * Test equals method with same type but different instance.
     */
    @Test
    public void testEqualsSameTypeDifferentInstance() {
        UnsupportedDurationField unsupported1 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField unsupported2 = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertFalse(unsupported1.equals(unsupported2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals("Milliseconds".hashCode(), unsupported.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertEquals("UnsupportedDurationField[Milliseconds]", unsupported.toString());
    }

    /**
     * Test readResolve method.
     */
    @Test
    public void testReadResolve() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField restored = (UnsupportedDurationField) unsupported.readResolve();
        assertEquals(unsupported, restored);
    }

    /**
     * Test unsupported method.
     */
    @Test
    public void testUnsupported() {
        UnsupportedDurationField unsupported = new UnsupportedDurationField(DurationFieldType.MILLISECONDS);
        assertThrows(UnsupportedOperationException.class, unsupported::unsupported);
    }
}