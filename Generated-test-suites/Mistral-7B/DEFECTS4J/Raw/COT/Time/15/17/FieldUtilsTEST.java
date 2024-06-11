package org.joda.time.field;

import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.field.FieldUtils.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        try {
            result = safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        try {
            result = safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = safeAdd(val1, val2);
        assertEquals(8, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            result = safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = safeSubtract(val1, val2);
        assertEquals(7L, result);

        val1 = 10L;
        val2 = 8L;
        result = safeSubtract(val1, val2);
        assertEquals(2L, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = 2;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply_longInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5L;
        val2 = Integer.MIN_VALUE;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply_longLong() {
        long val1 = 5L;
        long val2 = 3L;
        long result = safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 123L;
        int result = safeToInt(value);
        assertEquals(123, result);

        value = Long.MIN_VALUE;
        try {
            result = safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Long.MAX_VALUE;
        try {
            result = safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = safeMultiplyToInt(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            result = safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.instantOfDay();
        int value = 5;
        try {
            verifyValueBounds(fieldType, value, 0, 1);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(fieldType, value, 1, 1);
            // Expected pass
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }

        try {
            verifyValueBounds(fieldType, value, 1, 0);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = getWrappedValue(value, minValue, maxValue);
        assertEquals(2, wrappedValue);

        value = Integer.MIN_VALUE;
        minValue = 1;
        maxValue = 5;
        try {
            wrappedValue = getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = 5;
        minValue = 0;
        maxValue = Integer.MIN_VALUE;
        try {
            wrappedValue = getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        assertEquals(0, wrappedValue);
    }
}