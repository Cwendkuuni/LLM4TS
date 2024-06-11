package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.DateTimeFieldType;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(-2, FieldUtils.safeAdd(1, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeAddLong() {
        assertEquals(10L, FieldUtils.safeAdd(5L, 5L));
        assertEquals(-10L, FieldUtils.safeAdd(5L, -5L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1L, Long.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(3, 1));
        assertEquals(-2, FieldUtils.safeSubtract(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(-25, FieldUtils.safeMultiply(-2, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(25L, FieldUtils.safeMultiply(5L, 5));
        assertEquals(-125L, FieldUtils.safeMultiply(-5L, 5));
        assertEquals(-1L, FieldUtils.safeMultiply(1L, -1));
        assertEquals(0L, FieldUtils.safeMultiply(1L, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(25L, FieldUtils.safeMultiply(5L, 5L));
        assertEquals(-125L, FieldUtils.safeMultiply(-5L, 5L));
        assertEquals(-1L, FieldUtils.safeMultiply(1L, -1L));
        assertEquals(0L, FieldUtils.safeMultiply(1L, 0L));
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(12345, FieldUtils.safeMultiplyToInt(123, 101));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", 1, 31);

        FieldUtils.verifyValueBounds(field, 1, 1, 31);
        FieldUtils.verifyValueBounds(field, 31, 1, 31);
        FieldUtils.verifyValueBounds(field, 0, 1, 31);
        FieldUtils.verifyValueBounds(field, 32, 1, 31);

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();

        FieldUtils.verifyValueBounds(fieldType, 1, 1, 7);
        FieldUtils.verifyValueBounds(fieldType, 7, 1, 7);
        FieldUtils.verifyValueBounds(fieldType, 0, 1, 7);
        FieldUtils.verifyValueBounds(fieldType, 8, 1, 7);
    }

    @Test
    public void testVerifyValueBoundsString() {
        FieldUtils.verifyValueBounds("dayOfMonth", 1, 1, 31);
        FieldUtils.verifyValueBounds("dayOfMonth", 31, 1, 31);
        FieldUtils.verifyValueBounds("dayOfMonth", 0, 1, 31);
        FieldUtils.verifyValueBounds("dayOfMonth", 32, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 3, 0, 6));
        assertEquals(4, FieldUtils.getWrappedValue(1, 3, 0, 6));
        assertEquals(5, FieldUtils.getWrappedValue(1, 3, 0, 6));
        assertEquals(0, FieldUtils.getWrappedValue(-1, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(-1, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(-1, 3, 0, 6));
        assertEquals(0, FieldUtils.getWrappedValue(-7, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(-7, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(-7, 3, 0, 6));
        assertEquals(0, FieldUtils.getWrappedValue(-10, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(-10, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(-10, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(10, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(10, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(13, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(13, 3, 0, 6));
        assertEquals(3, FieldUtils.getWrappedValue(16, 3, 0, 6));
        assertEquals(6, FieldUtils.getWrappedValue(16, 3, 0, 6));

        assertEquals(1, FieldUtils.getWrappedValue(1, 6, 0, 3));
        assertEquals(4, FieldUtils.getWrappedValue(1, 6, 0, 3));
        assertEquals(5, FieldUtils.getWrappedValue(1, 6, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-1, 6, 0, 3));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 6, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-5, 6, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-7, 6, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-10, 6, 0, 3));
        assertEquals(5, FieldUtils.getWrappedValue(6, 6, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(13, 6, 0, 3));
        assertEquals(5, FieldUtils.getWrappedValue(16, 6, 0, 3));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;

        assertEquals(obj1, obj3);
        assertEquals(obj1, obj3);
        assertFalse(equals(null, obj1));
        assertFalse(equals(obj1, null));
        assertFalse(equals(obj1, obj2));
    }

}