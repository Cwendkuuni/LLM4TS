package org.joda.time.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAddInt() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeAddLong() {
        assertEquals(10L, FieldUtils.safeAdd(5L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5L, FieldUtils.safeSubtract(10L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testSafeMultiplyInt() {
        assertEquals(25, FieldUtils.safeMultiply(5, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(25L, FieldUtils.safeMultiply(5L, 5));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLong() {
        assertEquals(25L, FieldUtils.safeMultiply(5L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(5, FieldUtils.safeToInt(5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(25, FieldUtils.safeMultiplyToInt(5L, 5L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MAX_VALUE, 2L));
    }

    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = null; // initialize field
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 10, 0, 5));
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = null; // initialize fieldType
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 10, 0, 5));
    }

    @Test
    public void testVerifyValueBoundsString() {
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds("fieldName", 10, 0, 5));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(8, 5, 3));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(8, 5, 3));
    }

    @Test
    public void testEquals() {
        assertTrue(FieldUtils.equals("test", "test"));
        assertFalse(FieldUtils.equals("test", "Test"));
    }

}