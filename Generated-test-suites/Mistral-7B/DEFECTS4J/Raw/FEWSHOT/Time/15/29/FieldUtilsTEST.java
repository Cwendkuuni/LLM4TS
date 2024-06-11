package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-123, FieldUtils.safeNegate(123));
        assertEquals(-120, FieldUtils.safeNegate(120));
        assertEquals(-Integer.MIN_VALUE, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(5, FieldUtils.safeAdd(1, 4));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(-1, FieldUtils.safeAdd(-1, 1));
        assertEquals(-Integer.MIN_VALUE, FieldUtils.safeAdd(-1, Integer.MIN_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(-1, -1));
        assertEquals(-1, FieldUtils.safeAdd(-1, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(4, FieldUtils.safeSubtract(5, 1));
        assertEquals(-122, FieldUtils.safeSubtract(123, 1));
        assertEquals(-120, FieldUtils.safeSubtract(120, 20));
        assertEquals(-Integer.MIN_VALUE - 1, FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertEquals(-1, FieldUtils.safeSubtract(-1, 1));
        assertEquals(0, FieldUtils.safeSubtract(-1, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(-25, FieldUtils.safeMultiply(-5, 5));
        assertEquals(-1225, FieldUtils.safeMultiply(-5, 245));
        assertEquals(-12345, FieldUtils.safeMultiply(-5, 24567));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(-2147483647, 2147483647));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiply(2147483647, 2147483647));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        assertEquals(-25, FieldUtils.safeMultiply((long) -5, 5));
        assertEquals(-1225, FieldUtils.safeMultiply((long) -5, (long) 245));
        assertEquals(-12345, FieldUtils.safeMultiply((long) -5, (long) 24567));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 2147483647L));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(2147483647L, 2147483647L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 2L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2L));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        assertEquals(-625, FieldUtils.safeMultiply(5L, -125L));
        assertEquals(-1234500, FieldUtils.safeMultiply(245L, -5005L));
        assertEquals(-123456789000L, FieldUtils.safeMultiply(24567L, -500005L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE + 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE - 1L, Long.MAX_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(42, FieldUtils.safeToInt(42L));
        assertEquals(-42, FieldUtils.safeToInt(-42L));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(Long.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1L));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(2025, FieldUtils.safeMultiplyToInt(10, 202));
        assertEquals(-2025, FieldUtils.safeMultiplyToInt(-10, 202));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2147483647));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeMultiplyToInt(2147483647, Integer.MAX_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE + 1, 2147483647));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(2147483647, Integer.MIN_VALUE + 1));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.HOUR_OF_DAY, 10, null);

        FieldUtils.verifyValueBounds(field, -12, 0, 23);
        FieldUtils.verifyValueBounds(field, 24, 0, 23);
        FieldUtils.verifyValueBounds(field, 10, 0, 23);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, Integer.MIN_VALUE, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, Integer.MAX_VALUE, 0, 23));

        DateTimeFieldType fieldType = DateTimeFieldType.HOUR_OF_DAY;
        FieldUtils.verifyValueBounds(fieldType, -12, 0, 23);
        FieldUtils.verifyValueBounds(fieldType, 24, 0, 23);
        FieldUtils.verifyValueBounds(fieldType, 10, 0, 23);

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, Integer.MIN_VALUE, 0, 23));
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, Integer.MAX_VALUE, 0, 23));

        FieldUtils.verifyValueBounds("testField", -12, 0, 23);
        FieldUtils.verifyValueBounds("testField", 24, 0, 23);
        FieldUtils.verifyValueBounds("testField", 10, 0, 23);

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(null, 10, 0, 23));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(new DateTimeField(null, 10, null), 10, 0, 23));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(new DateTimeFieldType(null), 10, 0, 23));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds("testField", 10, 23, 0));
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds("testField", 23, 0, 10));
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(3, FieldUtils.getWrappedValue(3, 4, 0, 7));
        assertEquals(4, FieldUtils.getWrappedValue(7, 4, 0, 7));
        assertEquals(2, FieldUtils.getWrappedValue(-1, 4, 0, 7));
        assertEquals(6, FieldUtils.getWrappedValue(-4, 4, 0, 7));
        assertEquals(0, FieldUtils.getWrappedValue(-7, 4, 0, 7));
        assertEquals(5, FieldUtils.getWrappedValue(-2, 4, 0, 7));
        assertEquals(1, FieldUtils.getWrappedValue(-5, 4, 0, 7));

        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(4, 8, 0, 7));
    }
}