package org.joda.time.field.test;

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    
    
    // Test for safeNegate method
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = 0;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeAdd method
    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 3, sum;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1, sum); // no overflow

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = Integer.MAX_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeSubtract method
    @Test
    public void testSafeSubtract() {
        long val1 = 10, val2 = 5, diff;
        diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = Long.MIN_VALUE;
        try {
            diff = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeMultiply method
    @Test
    public void testSafeMultiply() {
        int val1 = 3, val2 = 2, product;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3;
        val2 = Integer.MAX_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for safeMultiplyToInt method
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3, val2 = 2, product;
        product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(6, (int) product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3;
        val2 = Long.MAX_VALUE;
        try {
            product = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Test for verifyValueBounds method
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test");
        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 28, 1, 28);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        FieldUtils.verifyValueBounds(field, 28, 1, 31);
    }

    // Test for getWrappedValue method
    @Test
    public void testGetWrappedValue() {
        int minValue = 0, maxValue = 5, currentValue, wrapValue = -1, expectedValue;

        // Test for positive input
        currentValue = 1;
        wrapValue = 3;
        expectedValue = 4;
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Test for negative input
        currentValue = -1;
        expectedValue = 4;
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Test for values near the minimum and maximum
        currentValue = minValue - 1;
        expectedValue = maxValue - (minValue % wrapValue);
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        currentValue = maxValue + 1;
        expectedValue = minValue + (maxValue % wrapValue);
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Test for minimum and maximum values
        currentValue = minValue;
        expectedValue = minValue;
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        currentValue = maxValue;
        expectedValue = maxValue;
        assertEquals(expectedValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Test invalid input (minValue >= maxValue)
        try {
            FieldUtils.getWrappedValue(1, 1, 1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Test for equals method
    @Test
    public void testEquals() {
        Object obj1 = new Object(), obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }

    
}