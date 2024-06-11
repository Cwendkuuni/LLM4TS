package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(6, FieldUtils.safeAdd(3, 3));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(4, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(12, FieldUtils.safeMultiply(3, 4));
        assertEquals(-12, FieldUtils.safeMultiply(-3, 4));
        assertEquals(-12, FieldUtils.safeMultiply(3, -4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(12, FieldUtils.safeMultiply(3, 4));
        assertEquals(-12, FieldUtils.safeMultiply(3, -4));
        assertEquals(-12, FieldUtils.safeMultiply(-3, 4));
        assertEquals(-12, FieldUtils.safeMultiply(-3, -4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(12, FieldUtils.safeMultiply(3, 4));
        assertEquals(-12, FieldUtils.safeMultiply(3, -4));
        assertEquals(-12, FieldUtils.safeMultiply(-3, 4));
        assertEquals(12, FieldUtils.safeMultiply(-3, -4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(45, FieldUtils.safeToInt(45L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(12, FieldUtils.safeMultiplyToInt(3, 4));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, -1, 1, 7));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 8, 1, 7));
        assertEquals(1, FieldUtils.verifyValueBounds(fieldType, 1, 1, 7));
        assertEquals(7, FieldUtils.verifyValueBounds(fieldType, 7, 1, 7));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 2, 0, 3));
        assertEquals(3, FieldUtils.getWrappedValue(4, 2, 0, 3));
        assertEquals(1, FieldUtils.getWrappedValue(-2, 2, 0, 3));
        assertEquals(2, FieldUtils.getWrappedValue(-3, 2, 0, 3));
        assertEquals(0, FieldUtils.getWrappedValue(1, 2, 0, 0));
        assertEquals(0, FieldUtils.getWrappedValue(-1, 2, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 0, 1, 1));
    }

    @Test
    public void testEquals() {
        assertEquals(true, FieldUtils.equals(1, 1));
        assertEquals(true, FieldUtils.equals(new Object(), new Object()));
        assertEquals(false, FieldUtils.equals(1, "hello"));
        assertEquals(false, FieldUtils.equals(null, 1));
    }
}