package org.joda.time.field.test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test with positive values
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        // Test with negative values
        value = -5;
        result = FieldUtils.safeNegate(value);
        assertEquals(5, result);

        // Test with minimum value
        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        // Test with positive values
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, result);

        // Test with negative values
        val1 = -2;
        val2 = 3;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(1, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        // Test with positive values
        long val1 = 10L;
        long val2 = 3L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, result);

        // Test with negative values
        val1 = 10L;
        val2 = -3L;
        result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(13L, result);

        // Test with overflow
        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        // Test with positive values
        int val1 = 3;
        int val2 = 4;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyWithLong() {
        // Test with positive values
        long val1 = 3L;
        int val2 = 4;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12L, result);

        // Test with overflow
        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyWithLongLong() {
        // Test with positive values
        long val1 = 3L;
        long val2 = 4L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(12L, result);

        // Test with overflow
        val1 = Long.MIN_VALUE;
        val2 = 2L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeToInt() {
        // Test with values within int range
        long value = 123;
        int result = FieldUtils.safeToInt(value);
        assertEquals(123, result);

        // Test with minimum int value
        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));

        // Test with maximum int value
        value = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3L;
        long val2 = 4L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(12, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        int value = 13;
        int lowerBound = 0;
        int upperBound = 10;

        // Test within bounds
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);

        // Test below lower bound
        value = 9;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound));

        // Test above upper bound
        value = 14;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound));
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;

        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value, result);

        value = -3;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(2, result);

        value = 6;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }
}