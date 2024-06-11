package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for FieldUtils.
 *
 * @author YourName
 */
public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Test with valid integers
        int value = 5;
        int expected = -value;
        int result = FieldUtils.safeNegate(value);
        assertEquals("safeNegate: invalid negated value for valid input", expected, result);

        // Test with Integer.MIN_VALUE
        value = Integer.MIN_VALUE;
        ArithmeticException exception = null;
        try {
            result = FieldUtils.safeNegate(value);
        } catch (ArithmeticException e) {
            exception = e;
        }
        assertNotNull("safeNegate: did not throw exception for Integer.MIN_VALUE", exception);
    }

    @Test
    public void testSafeAdd() {
        // Test with valid integers
        int val1 = 3;
        int val2 = 4;
        int expected = val1 + val2;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals("safeAdd: invalid result for valid inputs", expected, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        ArithmeticException exception = null;
        try {
            result = FieldUtils.safeAdd(val1, val2);
        } catch (ArithmeticException e) {
            exception = e;
        }
        assertNotNull("safeAdd: did not throw exception for overflow", exception);
    }

    @Test
    public void testSafeSubtract() {
        // Test with valid longs
        long val1 = 10L;
        long val2 = 5L;
        long expected = val1 - val2;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals("safeSubtract: invalid result for valid inputs", expected, result);

        // Test with overflow
        val1 = Long.MIN_VALUE;
        val2 = 1L;
        ArithmeticException exception = null;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
        } catch (ArithmeticException e) {
            exception = e;
        }
        assertNotNull("safeSubtract: did not throw exception for overflow", exception);
    }

    @Test
    public void testSafeMultiply() {
        // Test with valid integers
        int val1 = 3;
        int val2 = 4;
        int expected = val1 * val2;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals("safeMultiply: invalid result for valid inputs", expected, result);

        // Test with overflow
        val1 = Integer.MIN_VALUE;
        val2 = 2;
        ArithmeticException exception = null;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            exception = e;
        }
        assertNotNull("safeMultiply: did not throw exception for overflow", exception);
    }

    @Test
    public void testVerifyValueBounds() {
        // Test with valid inputs and bounds
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1);
        int value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 7);

        // Test with lower bound only
        FieldUtils.verifyValueBounds(field, value, 0, 7);
        value = 8;
        try {
            FieldUtils.verifyValueBounds(field, value, 0, 7);
            fail("VerifyValueBounds: did not throw exception for lower bound");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test with upper bound only
        value = 0;
        FieldUtils.verifyValueBounds(field, value, 1, 7);
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 0);
            fail("VerifyValueBounds: did not throw exception for upper bound");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test with invalid inputs
        value = 8;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 1);
            fail("VerifyValueBounds: did not throw exception for invalid input");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }
}