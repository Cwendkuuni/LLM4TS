package org.joda.time.field.util; // make sure the package is correct

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for FieldUtils methods.
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @org.junit.Test
    public void testSafeNegate() {
        // Test with valid value
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        // Test with minimum int value
        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative value
        value = -5;
        result = FieldUtils.safeNegate(value);
        assertEquals(5, result);
    }

    /**
     * Test for safeAdd method.
     */
    @org.junit.Test
    public void testSafeAdd() {
        // Test with valid values
        int a = 5;
        int b = 3;
        int result = FieldUtils.safeAdd(a, b);
        assertEquals(8, result);

        // Test with minimum int value
        a = Integer.MIN_VALUE;
        b = 1;
        result = FieldUtils.safeAdd(a, b);
        assertEquals(Integer.MIN_VALUE, result);

        // Test with overflow
        a = Integer.MIN_VALUE;
        b = Integer.MAX_VALUE;
        try {
            result = FieldUtils.safeAdd(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        a = -5;
        b = -3;
        result = FieldUtils.safeAdd(a, b);
        assertEquals(-2, result);
    }

    /**
     * Test for safeSubtract method.
     */
    @org.junit.Test
    public void testSafeSubtract() {
        // Test with valid values
        long a = 10;
        long b = 5;
        long result = FieldUtils.safeSubtract(a, b);
        assertEquals(5, result);

        // Test with minimum long value
        a = Long.MIN_VALUE;
        b = 1;
        result = FieldUtils.safeSubtract(a, b);
        assertEquals(Long.MIN_VALUE, result);

        // Test with overflow
        a = Long.MIN_VALUE;
        b = Long.MAX_VALUE;
        try {
            result = FieldUtils.safeSubtract(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        a = -10;
        b = -5;
        result = FieldUtils.safeSubtract(a, b);
        assertEquals(-5, result);
    }

    /**
     * Test for safeMultiply method.
     */
    @org.junit.Test
    public void testSafeMultiply() {
        // Test with valid values
        int a = 5;
        int b = 3;
        int result = FieldUtils.safeMultiply(a, b);
        assertEquals(15, result);

        // Test with integer overflow
        a = Integer.MIN_VALUE;
        b = 10;
        try {
            result = FieldUtils.safeMultiply(a, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        a = -5;
        b = 3;
        result = FieldUtils.safeMultiply(a, b);
        assertEquals(-15, result);
    }

    /**
     * Test for safeMultiply method with long type.
     */
    @org.junit.Test
    public void testSafeMultiplyLong() {
        // Test with valid values
        long a = 10;
        int b = 3;
        long result = FieldUtils.safeMultiply(a, b);
        assertEquals(30, result);

        // Test with long overflow
        long c = Long.MIN_VALUE;
        b = 10;
        try {
            result = FieldUtils.safeMultiply(c, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        long d = -10;
        b = 3;
        result = FieldUtils.safeMultiply(d, b);
        assertEquals(-30, result);

        // Test with int negative value and long positive value
        a = 10;
        b = -3;
        result = FieldUtils.safeMultiply(a, b);
        assertEquals(-30, result);

        // Test with long negative value and int positive value
        c = -10;
        b = 3;
        result = FieldUtils.safeMultiply(c, b);
        assertEquals(-30, result);
    }

    /**
     * Test for safeMultiply method with long and long types.
     */
    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        // Test with valid values
        long a = 10;
        long b = 3;
        long result = FieldUtils.safeMultiply(a, b);
        assertEquals(30, result);

        // Test with long overflow
        long c = Long.MIN_VALUE;
        b = 10;
        try {
            result = FieldUtils.safeMultiply(c, b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        long d = -10;
        long e = 3;
        result = FieldUtils.safeMultiply(d, e);
        assertEquals(-30, result);

        // Test with long negative value and long negative value
        long f = -10;
        long g = -3;
        result = FieldUtils.safeMultiply(f, g);
        assertEquals(30, result); // result should be positive because long is unsigned
    }

    /**
     * Test for safeToInt method.
     */
    @org.junit.Test
    public void testSafeToInt() {
        // Test with valid values
        long a = 10;
        int result = FieldUtils.safeToInt(a);
        assertEquals(10, result);

        // Test with long overflow
        long b = Long.MAX_VALUE + 1;
        try {
            result = FieldUtils.safeToInt(b);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        long c = -10;
        result = FieldUtils.safeToInt(c);
        assertEquals(-10, result);
    }

    /**
     * Test for safeMultiplyToInt method.
     */
    @org.junit.Test
    public void testSafeMultiplyToInt() {
        // Test with valid values
        long a = 10;
        long b = 3;
        int result = FieldUtils.safeMultiplyToInt(a, b);
        assertEquals(30, result);

        // Test with long overflow
        long c = Long.MAX_VALUE;
        long d = 10;
        try {
            result = FieldUtils.safeMultiplyToInt(c, d);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        // Test with negative values
        long e = -10;
        long f = 3;
        try {
            result = FieldUtils.safeMultiplyToInt(e, f);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for verifyValueBounds method with DateTimeField.
     */
    @org.junit.Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = DateTimeField.dayOfWeek();
        int value = 8; // Sunday
        int lowerBound = DateTimeFieldType.dayOfWeek().getValidValues().getMinimumValue();
        int upperBound = DateTimeFieldType.dayOfWeek().getValidValues().getMaximumValue();
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    /**
     * Test for verifyValueBounds method with DateTimeFieldType.
     */
    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        int value = 8; // Sunday
        int lowerBound = 1;
        int upperBound = 7;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with valid value
        value = 7; // Saturday
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    /**
     * Test for verifyValueBounds method with String field name.
     */
    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";
        int value = 8; // Sunday
        int lowerBound = DateTimeFieldType.dayOfWeek().getValidValues().getMinimumValue();
        int upperBound = DateTimeFieldType.dayOfWeek().getValidValues().getMaximumValue();
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        // Test with valid value
        value = 7; // Saturday
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }

    /**
     * Test for getWrappedValue method.
     */
    @org.junit.Test
    public void testGetWrappedValue() {
        // Test with valid values
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value, result);

        // Test with minimum value
        value = 0;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        // Test with maximum value
        value = maxValue;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        // Test with negative value
        value = -5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue - 5, result);

        // Test with large absolute value
        value = 100;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);
    }
}