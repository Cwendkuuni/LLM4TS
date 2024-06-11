package org.joda.time.field.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.joda.time.BaseDateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import org.joda.time.field.UnsupportedDurationField;

import org.junit.Test;

/**
 * Test class for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Test getInstance.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertEquals("unsupported fields should be equal", field1, field2);
        assertSame("unsupported fields should have same type", type, field1.getType());
    }

    /**
     * Test getInstance null type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    /**
     * Test getType.
     */
    @Test
    public void testgetType() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("unsupported fields should have type", type, field.getType());
    }

    /**
     * Test getName.
     */
    @Test
    public void testgetName() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("unsupported fields should have name", type.getName(), field.getName());
    }

    /**
     * Test isSupported.
     */
    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse("unsupported fields should not be supported", field.isSupported());
    }

    /**
     * Test isPrecise.
     */
    @Test
    public void testIsPrecise() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertTrue("unsupported fields should be precise", field.isPrecise());
    }

    /**
     * Test getValue.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValue() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 0;
        field.getValue(duration);
    }

    /**
     * Test getValueAsLong.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 0;
        field.getValueAsLong(duration);
    }

    /**
     * Test getValue(long, long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueLongLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 0;
        long instant = 0;
        field.getValue(duration, instant);
    }

    /**
     * Test getValueAsLong(long, long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetValueAsLongLongLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long duration = 0;
        long instant = 0;
        field.getValueAsLong(duration, instant);
    }

    /**
     * Test getMillis.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 0;
        field.getMillis(value);
    }

    /**
     * Test getMillis(long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 0;
        field.getMillis(value);
    }

    /**
     * Test getMillis(int, long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisIntLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        int value = 0;
        long instant = 0;
        field.getMillis(value, instant);
    }

    /**
     * Test getMillis(long, long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisLongLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long value = 0;
        long instant = 0;
        field.getMillis(value, instant);
    }

    /**
     * Test add.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        int value = 0;
        field.add(instant, value);
    }

    /**
     * Test add(long, long).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddLongLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long instant = 0;
        long value = 0;
        field.add(instant, value);
    }

    /**
     * Test getDifference.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 0;
        field.getDifference(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getDifferenceAsLong.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        long minuendInstant = 0;
        long subtrahendInstant = 0;
        field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
    }

    /**
     * Test getUnitMillis.
     */
    @Test
    public void testGetUnitMillis() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertEquals("unsupported fields should return 0", 0, field.getUnitMillis());
    }

    /**
     * Test compareTo.
     */
    @Test
    public void testCompareTo() {
        DurationFieldType type1 = DurationFieldType.dayOfWeek();
        DurationFieldType type2 = DurationFieldType.dayOfMonth();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type1);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type2);
        int comparison = field1.compareTo(field2);
        assertTrue("unsupported fields should compare as unequal", comparison != 0);
    }

    /**
     * Test equals.
     */
    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = UnsupportedDurationField.getInstance(type);
        assertTrue("unsupported fields should be equal", field1.equals(field2));
    }

    /**
     * Test equals null.
     */
    @Test
    public void testEqualsNull() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        assertFalse("unsupported fields should not be equal to null", field.equals(null));
    }

    /**
     * Test hashCode.
     */
    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        BaseDateTime baseDateTime = new BaseDateTime();
        assertEquals("unsupported fields should have same hashcode as baseDateTime", baseDateTime.hashCode(), field.hashCode());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        String expected = "UnsupportedDurationField[dayOfWeek]";
        assertEquals("unsupported fields should have expected string representation", expected, field.toString());
    }

    /**
     * Test readResolve.
     */
    @Test
    public void testReadResolve() throws IllegalAccessException, InstantiationException {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField field2 = (UnsupportedDurationField) field1.readResolve();
        assertEquals("unsupported fields should be equal after readResolve", field1, field2);
    }

    /**
     * Test unsupported.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupported() {
        DurationFieldType type = DurationFieldType.dayOfWeek();
        UnsupportedDurationField field = UnsupportedDurationField.getInstance(type);
        field.unsupported();
    }

}