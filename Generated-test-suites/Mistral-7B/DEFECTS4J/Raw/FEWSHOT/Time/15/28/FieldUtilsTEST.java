package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-2, FieldUtils.safeNegate(2));
        assertEquals(-3, FieldUtils.safeNegate(3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeAddLong() {
        assertEquals(3L, FieldUtils.safeAdd(1L, 2L));
        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals(10L, FieldUtils.safeAdd(5L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(1, 1));
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-1, FieldUtils.safeSubtract(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertEquals(-5, FieldUtils.safeMultiply(2, -2));
        assertEquals(10, FieldUtils.safeMultiply(3, 3));
        assertEquals(-10, FieldUtils.safeMultiply(3, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(9L, FieldUtils.safeMultiply(3L, 3L));
        assertEquals(-9L, FieldUtils.safeMultiply(3L, -3L));
        assertEquals(15L, FieldUtils.safeMultiply(5L, 3L));
        assertEquals(-15L, FieldUtils.safeMultiply(5L, -3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2L));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(5, FieldUtils.safeMultiplyToInt(2, 2));
        assertEquals(-5, FieldUtils.safeMultiplyToInt(2, -2));
        assertEquals(10, FieldUtils.safeMultiplyToInt(3, 3));
        assertEquals(-10, FieldUtils.safeMultiplyToInt(3, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField testField = new DateTimeField(DateTimeFieldType.millis(), "test", 0, Long.MIN_VALUE, Long.MAX_VALUE, null);

        // Test valid values
        FieldUtils.verifyValueBounds(testField, 0, 0, 100);
        FieldUtils.verifyValueBounds(testField, 50, 0, 100);
        FieldUtils.verifyValueBounds(testField, 100, 0, 100);

        // Test invalid values
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(testField, 101, 0, 100));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(testField, Integer.MIN_VALUE, 0, 100));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(testField, Integer.MAX_VALUE + 1, 0, 100));
    }

    @Test
    public void testGetWrappedValue() {
        // Test positive values
        assertEquals(1, FieldUtils.getWrappedValue(3, 2, 0, 5));
        assertEquals(4, FieldUtils.getWrappedValue(7, 2, 0, 5));
        assertEquals(2, FieldUtils.getWrappedValue(4, 2, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(8, 2, 0, 5));

        // Test zero value
        assertEquals(0, FieldUtils.getWrappedValue(0, 2, 0, 5));

        // Test negative values
        assertEquals(4, FieldUtils.getWrappedValue(-2, 2, 0, 5));
        assertEquals(1, FieldUtils.getWrappedValue(-5, 2, 0, 5));
        assertEquals(3, FieldUtils.getWrappedValue(-6, 2, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-8, 2, 0, 5));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals(null, null));
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals(null, "test"));
        assertFalse(FieldUtils.equals("test1", "test2"));
    }
}