package org.joda.time.field.util; // adjust the package name if necessary

import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests negating an integer.
     */
    @Test
    public void testSafeNegate() {
        // Positive numbers
        int value = 10;
        int expected = -10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        // Minimum integer value
        value = Integer.MIN_VALUE;
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
        String expectedMessage = "Integer.MIN_VALUE cannot be negated";
        assertEquals(expectedMessage, exception.getMessage());

        // Negative numbers
        value = -10;
        expected = 10;
        result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);
    }

    /**
     * Tests adding two integers.
     */
    @Test
    public void testSafeAdd() {
        // Add two positive numbers
        int a = 10;
        int b = 5;
        int expected = 15;
        int result = FieldUtils.safeAdd(a, b);
        assertEquals(expected, result);

        // Add an integer to itself
        a = 5;
        expected = 10;
        result = FieldUtils.safeAdd(a, a);
        assertEquals(expected, result);

        // Add an integer to a minimum value
        a = Integer.MIN_VALUE;
        b = 5;
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));
        String expectedMessage = "The calculation caused an overflow: " + a + " + " + b;
        assertEquals(expectedMessage, exception.getMessage());

        // Add an integer to a maximum value
        a = Integer.MAX_VALUE;
        b = 5;
        expected = Integer.MAX_VALUE;
        result = FieldUtils.safeAdd(a, b);
        assertEquals(expected, result);
    }

    /**
     * Tests multiplying two integers.
     */
    @Test
    public void testSafeMultiply() {
        // Multiply two positive numbers
        int a = 10;
        int b = 5;
        int expected = 50;
        int result = FieldUtils.safeMultiply(a, b);
        assertEquals(expected, result);

        // Multiply a positive number by zero
        a = 10;
        b = 0;
        expected = 0;
        result = FieldUtils.safeMultiply(a, b);
        assertEquals(expected, result);

        // Multiply a negative number by a positive number
        a = -10;
        b = 5;
        expected = -50;
        result = FieldUtils.safeMultiply(a, b);
        assertEquals(expected, result);

        // Multiply a number by a maximum value
        a = 10;
        b = Integer.MAX_VALUE;
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));
        String expectedMessage = "Multiplication overflows an int: " + a + " * " + b;
        assertEquals(expectedMessage, exception.getMessage());
    }

    /**
     * Tests the {@code verifyValueBounds} method.
     */
    @Test
    public void testVerifyValueBounds() {
        // Test with DateTimeField
        DateTimeField field = new DateTimeField(DateTimeFieldType.DAY_OF_MONTH, 1, DateTimeUnits.DAYS) {
            @Override
            public int getValue() {
                return 31;
            }
        };

        // Test with lower bound
        int value = 20;
        int lowerBound = 1;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with upper bound
        value = 42;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 31);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with DateTimeFieldType
        DateTimeFieldType fieldType = DateTimeFieldType.HOUR_OF_DAY;

        // Test with lower bound
        value = 24;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with upper bound
        value = -1;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with a String field name
        String fieldName = "hourOfDay";

        // Test with lower bound
        value = 25;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with upper bound
        value = -1;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }
}