package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAddIntInt() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(100, FieldUtils.safeAdd(45, 55));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(2147483647, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-2147483648, 1));
    }

    @Test
    public void testSafeAddLongLong() {
        assertEquals(3, (int) FieldUtils.safeAdd(1L, 2L));
        assertEquals(100, (int) FieldUtils.safeAdd(45L, 55L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testSafeSubtractLongLong() {
        assertEquals(2, (int) FieldUtils.safeSubtract(3, 1));
        assertEquals(-99, (int) FieldUtils.safeSubtract(100, 99));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(-2147483648, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(2147483647, 1L));
    }

    @Test
    public void testSafeMultiplyIntInt() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(-15, FieldUtils.safeMultiply(3, -5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertEquals(-15, FieldUtils.safeMultiply(3, -5));
        assertEquals(1000, FieldUtils.safeMultiply(2, 500));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(1, FieldUtils.safeMultiply(1, 1));
        assertEquals(-1, FieldUtils.safeMultiply(-1, 1));
        assertEquals(-1, FieldUtils.safeMultiply(1, -1));
        assertEquals(1024, FieldUtils.safeMultiply(2, 512));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(42, FieldUtils.safeToInt(42L));
        assertEquals(43, FieldUtils.safeToInt(43L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE - 1L));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(12, FieldUtils.safeMultiplyToInt(4, 3));
        assertEquals(-24, FieldUtils.safeMultiplyToInt(4, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();

        assertDoesNotThrow(() -> FieldUtils.verifyValueBounds(fieldType, 31, 1, 31));
        assertDoesNotThrow(() -> FieldUtils.verifyValueBounds(fieldType, 1, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 32, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 1, 31));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(6, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-4, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(6, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 0, 5));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MIN_VALUE, 0, 5));
        assertEquals(0, FieldUtils.getWrappedValue(Integer.MAX_VALUE, 0, 5));
    }

    @Test
    public void testEquals() {
        assertEquals(1, FieldUtils.equals(1, 1));
        assertEquals(true, FieldUtils.equals((Object) 1, 1));
        assertEquals(true, FieldUtils.equals(1, (Object) 1));
        assertFalse(FieldUtils.equals(1, "two"));
        assertFalse(FieldUtils.equals(null, 1));
        assertFalse(FieldUtils.equals(1, null));
    }
}