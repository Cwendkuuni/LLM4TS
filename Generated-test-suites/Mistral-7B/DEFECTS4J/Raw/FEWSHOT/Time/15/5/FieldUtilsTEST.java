package org.joda.time.field;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
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
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(-2, FieldUtils.safeAdd(-1, -1));
        assertEquals(Integer.MAX_VALUE + 1, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(-1, Integer.MAX_VALUE + 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-2, FieldUtils.safeSubtract(0, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertEquals(-120, FieldUtils.safeMultiply(-4, 30));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiply_longInt() {
        assertEquals(10, FieldUtils.safeMultiply(5, 2));
        assertEquals(-200, FieldUtils.safeMultiply(-10, 20));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
    }

    @Test
    public void testSafeMultiply_intLong() {
        assertEquals(10, FieldUtils.safeMultiply(5, 2L));
        assertEquals(-200, FieldUtils.safeMultiply(-10, 20L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(1, FieldUtils.safeToInt(1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(Long.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Long.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(10, FieldUtils.safeMultiplyToInt(5, 2));
        assertEquals(-200, FieldUtils.safeMultiplyToInt(-10, 20));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, 2));
    }

    @Test
    public void testIllegalFieldValueException() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 32);
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 33, 1, 31));
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 25, 0, 23));
        String fieldName = "dayOfWeek";
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 8, 1, 7));
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 1;
        int maxValue = 5;

        int currentValue = 1;
        assertEquals(1, FieldUtils.getWrappedValue(currentValue, 0, minValue, maxValue));

        currentValue = 6;
        assertEquals(1, FieldUtils.getWrappedValue(currentValue, -1, minValue, maxValue));

        currentValue = 7;
        assertEquals(5, FieldUtils.getWrappedValue(currentValue, 1, minValue, maxValue));

        currentValue = 0;
        assertEquals(1, FieldUtils.getWrappedValue(currentValue, 4, minValue, maxValue));

        currentValue = 6;
        assertEquals(1, FieldUtils.getWrappedValue(currentValue, 6, minValue, maxValue));

        currentValue = Integer.MIN_VALUE - 1;
        assertEquals(minValue, FieldUtils.getWrappedValue(currentValue, -1, minValue, maxValue));

        currentValue = Integer.MIN_VALUE;
        assertEquals(maxValue, FieldUtils.getWrappedValue(currentValue, 1, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        assertFalse(FieldUtils.equals(1, "two"));
        assertFalse(FieldUtils.equals(null, new Object()));
        assertTrue(FieldUtils.equals(1, 1));
        assertTrue(FieldUtils.equals("one", "one"));
    }
}