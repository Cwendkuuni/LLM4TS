package org.joda.time.field; // Make sure to import necessary classes

import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.field.FieldUtils.*;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        try {
            safeNegate(value);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        try {
            safeAdd(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        long lVal1 = Long.MIN_VALUE;
        long lVal2 = 1;
        long sum = safeAdd(lVal1, lVal2);
        Assert.assertEquals(lVal1 + lVal2, sum);
    }

    @Test
    public void testSafeSubtract() {
        long val1 = Long.MAX_VALUE;
        long val2 = 1;
        try {
            safeSubtract(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        long diff = safeSubtract(Long.MAX_VALUE, 1);
        Assert.assertEquals(Long.MAX_VALUE - 1, diff);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = Integer.MIN_VALUE;
        int val2 = Integer.MAX_VALUE;
        try {
            safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        int mult = safeMultiply(1, 10);
        Assert.assertEquals(10, mult);

        long lVal1 = Long.MIN_VALUE;
        long lVal2 = Long.MAX_VALUE;
        long mult2 = safeMultiply(lVal1, lVal2);
        Assert.assertEquals(mult2 % lVal2, lVal1);
    }

    @Test
    public void testSafeMultiplyLongToInt() {
        long val1 = 1;
        int val2 = Integer.MIN_VALUE;
        try {
            safeMultiply(val1, val2);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        long mult = safeMultiply(1, 10);
        Assert.assertEquals(mult, 10);

        long mult3 = safeMultiply(10, 5);
        Assert.assertEquals(mult3, 50);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 31, 1, 2022);

        try {
            verifyValueBounds(field, 0, 30, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(field, 32, 30, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(field, 31, 30, 31);
            Assert.fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            verifyValueBounds(field, 31, 31, 31);
            Assert.fail("Expected no exception");
        } catch (IllegalFieldValueException e) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 1;
        int maxValue = 5;
        int value = minValue - 1;
        int wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        value = maxValue + 1;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        value = minValue;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        value = maxValue;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(maxValue, wrappedValue);

        value = minValue + maxValue;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        value = maxValue - minValue;
        wrappedValue = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(maxValue, wrappedValue);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;

        Assert.assertFalse(equals(obj1, null));
        Assert.assertFalse(equals(null, obj1));
        Assert.assertFalse(equals(obj1, obj2));
        Assert.assertTrue(equals(obj1, obj3));
    }
}