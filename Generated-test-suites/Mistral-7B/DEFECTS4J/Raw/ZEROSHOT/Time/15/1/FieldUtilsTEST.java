package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 7;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(10, result);

        val1 = -5;
        val2 = 3;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(-2, result);

        val1 = Integer.MIN_VALUE;
        val2 = 10;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 10;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7, result);

        val1 = 5;
        val2 = 2;
        result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(3, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 2;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = -5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5;
        val2 = -3;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 2;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeToInt() {
        long value = 5;
        int result = FieldUtils.safeToInt(value);
        assertEquals(5, result);

        value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "field");

        int value = 30;
        int lowerBound = 1;
        int upperBound = 31;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 28;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = 0;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        field = new DateTimeField(DateTimeFieldType.hourOfDay(), "field");
        value = 24;
        FieldUtils.verifyValueBounds(field, value, 0, 23);
        FieldUtils.verifyValueBounds(field, value, 23, 23);
        FieldUtils.verifyValueBounds(field, value, 24, 23);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 3;
        int minValue = 1;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, wrappedValue);

        value = 8;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }
}