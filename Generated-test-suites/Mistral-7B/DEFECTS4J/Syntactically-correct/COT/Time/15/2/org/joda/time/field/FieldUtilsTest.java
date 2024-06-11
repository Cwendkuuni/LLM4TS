package org.joda.time.field.test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.field.FieldUtils.*;

/**
 * Unit tests for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = safeNegate(value);
        Assert.assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> safeNegate(value));
    }

    /**
     * Test safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 2;
        int sum = 5;
        int result = safeAdd(val1, val2);
        Assert.assertEquals(sum, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        Assert.assertThrows(ArithmeticException.class, () -> safeAdd(val1, val2));

        val1 = 3;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> safeAdd(val1, val2));
    }

    /**
     * Test safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = 5L;
        long result = safeSubtract(val1, val2);
        Assert.assertEquals(diff, result);

        val1 = 10L;
        val2 = 15L;
        Assert.assertThrows(ArithmeticException.class, () -> safeSubtract(val1, val2));
    }

    /**
     * Test safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 2;
        int product = 6;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(product, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        Assert.assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));

        val1 = 3;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));
    }

    /**
     * Test verifyValueBounds method.
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "DOW", null);

        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;
        verifyValueBounds(field, value, lowerBound, upperBound);

        value = 32;
        Assert.assertThrows(IllegalFieldValueException.class, () -> verifyValueBounds(field, value, lowerBound, upperBound));

        value = 1;
        verifyValueBounds(field, value, lowerBound, upperBound);
    }

    /**
     * Test getWrappedValue method.
     */
    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(value, result);

        value = 6;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(minValue, result);

        value = 4;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(value, result);

        minValue = maxValue;
        Assert.assertThrows(IllegalArgumentException.class, () -> getWrappedValue(value, minValue, maxValue));
    }
}