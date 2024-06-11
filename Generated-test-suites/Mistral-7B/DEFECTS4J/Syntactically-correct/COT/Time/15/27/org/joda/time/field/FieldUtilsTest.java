package org.joda.time.field; // Make sure to import all necessary classes

import org.junit.Test;
import org.junit.Assert.*; // For Assert.* methods

import static org.joda.time.field.FieldUtils.*; // For static import of FieldUtils methods

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int input = 5;
        int expected = -5;
        int result = safeNegate(input);
        assertEquals(expected, result);

        input = Integer.MIN_VALUE;
        try {
            result = safeNegate(input);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3, val2 = 4, expected = 7;
        long result = safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    // Add more test cases for safeSubtract, safeMultiply, and other methods...

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3, val2 = 4, expected = 12;
        int result = safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MAX_VALUE;
        val2 = 2;
        try {
            result = safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31, 1, 31);
        int value = 32;

        try {
            verifyValueBounds(field, value, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 11, wrapValue = 5, minValue = 0, maxValue = 10;
        int expected = 3;
        int result = getWrappedValue(value, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        wrapValue = 1;
        try {
            result = getWrappedValue(value, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Add more test cases for other FieldUtils methods and edge cases...
}