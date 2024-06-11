package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.junit.Test;

/**
 * Test class for UnsupportedDurationField
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)}
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField instance1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField instance2 = UnsupportedDurationField.getInstance(type);
        assertEquals(instance1, instance2);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getInstance(DurationFieldType)} with null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullArgument() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getName()}
     */
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("Millis", unsupported.getName());
    }

    /**
     * Test method for {@link UnsupportedDurationField#isSupported()}
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValue(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValue(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getValueAsLong(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getValueAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisInt() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(int, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getMillis(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getMillis(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, int)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongInt() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1L, 1);
    }

    /**
     * Test method for {@link UnsupportedDurationField#add(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.add(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifference(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifference(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getDifferenceAsLong(long, long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLongLong() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        unsupported.getDifferenceAsLong(1L, 1L);
    }

    /**
     * Test method for {@link UnsupportedDurationField#getUnitMillis()}
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        long unitMillis = unsupported.getUnitMillis();
        assertEquals(0, unitMillis);
    }

    /**
     * Test method for {@link UnsupportedDurationField#compareTo(DurationField)}
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.MILLIS;
        DurationFieldType type2 = DurationFieldType.SECONDS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type2);
        int comparisonResult = unsupported1.compareTo(unsupported2);
        assertEquals(0, comparisonResult);
    }

    /**
     * Test method for {@link UnsupportedDurationField#equals(Object)}
     */
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(type);
        assertTrue(unsupported1.equals(unsupported2));

        Object other = new Object();
        assertFalse(unsupported1.equals(other));
    }

    /**
     * Test method for {@link UnsupportedDurationField#hashCode()}
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        int hashCode = unsupported.hashCode();
        assertEquals(-1244382685, hashCode);
    }

    /**
     * Test method for {@link UnsupportedDurationField#toString()}
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.MILLIS;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        String toString = unsupported.toString();
        assertNull(toString);
    }

}