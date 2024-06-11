package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(0, FieldUtils.safeNegate(0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE + 1));
    }

    @Test
    public void testSafeAdd_long() {
        assertEquals(3L, FieldUtils.safeAdd(1L, 2L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1L, Long.MAX_VALUE + 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Long.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiply_longInt() {
        assertEquals(10L, FieldUtils.safeMultiply(2L, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1L, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_longLong() {
        assertEquals(30L, FieldUtils.safeMultiply(2L, 15L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1L, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(127, FieldUtils.safeToInt(127L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(60, FieldUtils.safeMultiplyToInt(20, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", null);

        assertFalse(FieldUtils.class.isInstance(null));
        FieldUtils.verifyValueBounds(field, 32, 1, 31);
        FieldUtils.verifyValueBounds(field, 1, 1, 31);
        FieldUtils.verifyValueBounds(field, 33, 1, 31);

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        FieldUtils.verifyValueBounds(fieldType, 8, 1, 7);
        FieldUtils.verifyValueBounds(fieldType, 1, 1, 7);
        FieldUtils.verifyValueBounds(fieldType, 8, 1, 7);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(4, FieldUtils.getWrappedValue(5, 1, 4));
        assertEquals(0, FieldUtils.getWrappedValue(0, 1, 4));
        assertEquals(3, FieldUtils.getWrappedValue(5, 1, 4));
        assertEquals(3, FieldUtils.getWrappedValue(-2, 1, 4));
        assertEquals(0, FieldUtils.getWrappedValue(-5, 1, 4));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(1, 1));
        assertFalse(FieldUtils.equals(1, 2));
        assertFalse(FieldUtils.equals(null, 1));
        assertFalse(FieldUtils.equals(1, null));
    }
}