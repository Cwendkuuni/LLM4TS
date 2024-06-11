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
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(1000000, FieldUtils.safeAdd(999999, 1001));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(2, FieldUtils.safeSubtract(3, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(6, FieldUtils.safeMultiply(2, 3));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply_int_long() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2L));
    }

    @Test
    public void testSafeMultiply_long_int() {
        assertEquals(10, FieldUtils.safeMultiply(2L, 5));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2L, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply_long_long() {
        assertEquals(25, FieldUtils.safeMultiply(5L, 5L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(-1L, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2L, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2L));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(42, FieldUtils.safeToInt(42L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MAX_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(1215, FieldUtils.safeMultiplyToInt(5, 243));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MAX_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType type = DateTimeFieldType.dayOfWeek();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(type, 7, 1, 6));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(type, 1, 8, 6));

        DateTimeField field = new DateTimeField(type, "dayOfWeek", 2015, 1, 1);
        FieldUtils.verifyValueBounds(field, 1, 1, 7);
        FieldUtils.verifyValueBounds(field, 2, 1, 7);
        FieldUtils.verifyValueBounds(field, 7, 1, 7);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 1, 7));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 8, 1, 7));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 0, 10));
        assertEquals(11, FieldUtils.getWrappedValue(12, 0, 10));
        assertEquals(0, FieldUtils.getWrappedValue(-1, 0, 10));
        assertEquals(10, FieldUtils.getWrappedValue(11, 0, 10));
        assertEquals(0, FieldUtils.getWrappedValue(-11, 0, 10));
        assertEquals(5, FieldUtils.getWrappedValue(15, 0, 10));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 0, 10));

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(0, 11, 10));
    }
}