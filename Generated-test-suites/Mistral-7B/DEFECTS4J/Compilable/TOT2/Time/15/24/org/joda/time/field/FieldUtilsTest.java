package org.junit.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.field.FieldUtils;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            negatedValue = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Add test cases for edge cases and other scenarios (e.g., negative values, two's complement) here...
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        long sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Add test cases for edge cases and other scenarios (e.g., overflow, underflow) here...
    }

    // Add test methods for other methods (safeSubtract, safeMultiply, etc.) here...

    // Add tests for specific use cases, edge cases, and error scenarios for each method here...
}