package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;
import org.joda.time.UnsupportedOperationException;

/**
 * Tests for {@code UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test for getInstance() method.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals(field1, field2);
    }

    /**
     * Test for getType() method.
     */
    @Test
    public void testGetType() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(type, field.getType());
    }

    /**
     * Test for getName() method.
     */
    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("instantSeconds", field.getName());
    }

    /**
     * Test for isSupported() method.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.isSupported());
    }

    /**
     * Test for isPrecise() method.
     */
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.isPrecise());
    }

    /**
     * Test for getValue() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1234567890123L);
    }

    /**
     * Test for getValueAsLong() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1234567890123L);
    }

    /**
     * Test for getValue() method with instant parameter.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValue(1234567890123L, 1234567890123L);
    }

    /**
     * Test for getValueAsLong() method with instant parameter.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getValueAsLong(1234567890123L, 1234567890123L);
    }

    /**
     * Test for getMillis() method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_int() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(5);
    }

    /**
     * Test for getMillis() method with long parameter.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1234567890123L);
    }

    /**
     * Test for getMillis() method with instant and int parameters.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_int() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1234567890123L, 5);
    }

    /**
     * Test for getMillis() method with instant and long parameters.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getMillis(1234567890123L, 1234567890123L);
    }

    /**
     * Test for add() method with instant parameter.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1234567890123L, 5);
    }

    /**
     * Test for add() method with instant and long parameters.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.add(1234567890123L, 1234567890123L);
    }

    /**
     * Test for getDifference() method with long instant1 and long instant2.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifference(1234567890123L, 1234567890123L);
    }

    /**
     * Test for getDifferenceAsLong() method with long instant1 and long instant2.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong_long_long() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.getDifferenceAsLong(1234567890123L, 1234567890123L);
    }

    /**
     * Test for getUnitMillis() method.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals(0, field.getUnitMillis());
    }

    /**
     * Test for compareTo() method.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType type2 = DurationFieldType.INSTANT_MILLIS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertEquals(0, field1.compareTo(field2));
    }

    /**
     * Test for equals() method with null object.
     */
    @Test
    public void testEquals_null() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse(field.equals(null));
    }

    /**
     * Test for equals() method with different object.
     */
    @Test
    public void testEquals_differentObject() {
        DurationFieldType type1 = DurationFieldType.INSTANT_SECONDS;
        DurationFieldType type2 = DurationFieldType.DAYS;
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        assertFalse(field1.equals(field2));
    }

    /**
     * Test for equals() method with same instance.
     */
    @Test
    public void testEquals_sameInstance() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue(field.equals(field));
    }

    /**
     * Test for hashCode() method.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int hashCode = field.hashCode();
        DurationFieldType type2 = DurationFieldType.INSTANT_MILLIS;
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        int hashCode2 = field2.hashCode();
        assertNotSame(hashCode, hashCode2);

        field = UnsupportedDurationField.getInstance(type);
        hashCode = field.hashCode();
        field = UnsupportedDurationField.getInstance(type);
        assertEquals(hashCode, field.hashCode());
    }

    /**
     * Test for toString() method.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.INSTANT_SECONDS;
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String string = field.toString();
        assertTrue(string.startsWith("UnsupportedDurationField["));
        assertTrue(string.endsWith("]"));
    }
}