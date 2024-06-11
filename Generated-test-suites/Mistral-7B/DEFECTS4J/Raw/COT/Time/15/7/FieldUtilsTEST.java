package org.joda.time.field; // Import the class under test

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 2L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, result);

        val1 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 2L;
        long val2 = 3L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10L;
        int result = FieldUtils.safeToInt(value);
        assertEquals(10, result);

        value = Integer.MIN_VALUE + 1;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 2L;
        long val2 = 3L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(6, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyValueBoundsWithInvalidBounds() {
        DateTimeField field = null; // Replace with a valid DateTimeField
        int value = 5;
        int lowerBound = 6;
        int upperBound = 5;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        fail("Expected IllegalFieldValueException");
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 0;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
        assertEquals(2, result);

        value = -10;
        minValue = 0;
        maxValue = 5;
        result = FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
        assertEquals(2, result);

        value = Integer.MIN_VALUE;
        minValue = 0;
        maxValue = Integer.MAX_VALUE;
        try {
            result = FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        minValue = maxValue; // Make invalid bounds
        try {
            result = FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj2)); // Should not throw an exception
        assertFalse(FieldUtils.equals(obj1, null));
    }

    
}