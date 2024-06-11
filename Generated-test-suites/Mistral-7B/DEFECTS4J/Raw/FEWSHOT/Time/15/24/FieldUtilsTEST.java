package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

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
    public void testSafeAdd() {
        assertEquals(3, FieldUtils.safeAdd(1, 2));
        assertEquals(1000000000000L, FieldUtils.safeAdd(Long.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(3, FieldUtils.safeSubtract(4, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(-1, Integer.MAX_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(5, FieldUtils.safeMultiply(2, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(2, Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyLongToInt() {
        assertEquals(10, FieldUtils.safeMultiply(5, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-5, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(5, -2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-5, -2));
    }

    @Test
    public void testSafeMultiplyLongToLong() {
        assertEquals(100L, FieldUtils.safeMultiply(5L, 20L));
        assertEquals(-100L, FieldUtils.safeMultiply(-5L, 20L));
        assertEquals(100L, FieldUtils.safeMultiply(5L, -20L));
        assertEquals(-100L, FieldUtils.safeMultiply(-5L, -20L));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(123, FieldUtils.safeToInt(123L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MAX_VALUE + 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE - 1L));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(12345, FieldUtils.safeMultiplyToInt(10, 1235));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", null);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 8, 1, 7));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 1, 8, 7));
        assertEquals(0, FieldUtils.verifyValueBounds(field, 1, 1, 7).getValue());
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(null, 1, 1, 7));
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 33, 1, 31));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 1, 32, 31));
        assertEquals(1, FieldUtils.verifyValueBounds(fieldType, 1, 1, 31).getValue());
    }

    @Test
    public void testVerifyValueBoundsString() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(null, 1, 1, 7));
        String fieldName = "dayOfWeek";
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 8, 1, 7));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 1, 8, 7));
        assertEquals(0, FieldUtils.verifyValueBounds(fieldName, 1, 1, 7).getValue());
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(1, 2, 0, 5));
        assertEquals(2, FieldUtils.getWrappedValue(5, 2, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(8, 2, 0, 5));
        assertEquals(0, FieldUtils.getWrappedValue(6, 2, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-1, 2, 0, 5));
        assertEquals(0, FieldUtils.getWrappedValue(-6, 2, 0, 5));
    }

    @Test
    public void testEquals() {
        assertEquals("java.lang.Integer@45d6174c", FieldUtils.equals(null, new Object() {}).toString());
        assertEquals("java.lang.Integer@45d6174c", FieldUtils.equals(null, null).toString());
        Object obj = new Object();
        assertEquals(obj.toString(), FieldUtils.equals(obj, obj).toString());
        assertFalse(FieldUtils.equals(obj, new Object() {}).toString());
    }
}