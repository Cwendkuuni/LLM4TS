package org.joda.time.field;

import org.junit.Assert;
import org.junit.Test;

import static org.joda.time.field.FieldUtils.*;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = safeNegate(value);
        Assert.assertEquals(-value, result);

        value = -5;
        result = safeNegate(value);
        Assert.assertEquals(value, result);

        value = Integer.MIN_VALUE;
        try {
            result = safeNegate(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = safeAdd(val1, val2);
        Assert.assertEquals(val1 + val2, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = safeAdd(val1, val2);
        Assert.assertEquals(val1 + val2, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = safeAdd(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAddWithNegativeSecondValue() {
        int val1 = 5;
        int val2 = -3;
        int result = safeAdd(val1, val2);
        Assert.assertEquals(val1 + val2, result);
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = safeSubtract(val1, val2);
        Assert.assertEquals(val1 - val2, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = safeSubtract(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyWithNegativeSecondValue() {
        int val1 = 5;
        int val2 = -3;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeMultiplyWithOneOfValuesBeingZero() {
        int val1 = 5;
        int val2 = 0;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(0, result);

        val1 = 0;
        val2 = 5;
        result = safeMultiply(val1, val2);
        Assert.assertEquals(0, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyWithBothValuesBeingNegative() {
        int val1 = -5;
        int val2 = -3;
        int result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeMultiplyLongWithInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeMultiplyLongWithNegativeInt() {
        long val1 = 5L;
        int val2 = -3;
        long result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeMultiplyLongWithZero() {
        long val1 = 5L;
        int val2 = 0;
        try {
            long result = safeMultiply(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongWithNegativeLong() {
        long val1 = 5L;
        long val2 = -3L;
        long result = safeMultiply(val1, val2);
        Assert.assertEquals(val1 * val2, result);
    }

    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = safeToInt(value);
        Assert.assertEquals(Integer.MAX_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            result = safeToInt(value);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = safeMultiplyToInt(val1, val2);
        Assert.assertEquals(val1 * val2, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = safeMultiplyToInt(val1, val2);
            Assert.fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 32);
        try {
            verifyValueBounds(field, 33, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            verifyValueBounds(field, 31, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        verifyValueBounds(field, 31, 1, 32);
    }

    @Test
    public void testVerifyValueBoundsWithStringFieldName() {
        String fieldName = "dayOfMonth";
        try {
            verifyValueBounds(fieldName, 33, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            verifyValueBounds(fieldName, 31, 1, 31);
            Assert.fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }

        verifyValueBounds(fieldName, 31, 1, 32);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 12;
        int minValue = 5;
        int maxValue = 20;
        int result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals(value % (maxValue - minValue + 1) + minValue, result);

        value = -12;
        result = getWrappedValue(value, minValue, maxValue);
        Assert.assertEquals((maxValue - (-value) % (maxValue - minValue + 1)) + minValue, result);

        minValue = maxValue;
        try {
            result = getWrappedValue(value, minValue, maxValue);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Assert.assertFalse(equals(obj1, obj2));

        obj1 = obj2;
        Assert.assertTrue(equals(obj1, obj2));

        obj1 = null;
        Assert.assertFalse(equals(obj1, obj2));
    }

    
}