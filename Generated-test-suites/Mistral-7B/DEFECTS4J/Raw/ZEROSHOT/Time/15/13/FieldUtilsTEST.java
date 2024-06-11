package org.joda.time.test.field;

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.test.DateTimeFieldTestCase;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest extends DateTimeFieldTestCase {

    /**
     * Test safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negated = FieldUtils.safeNegate(value);
        assertEquals(-5, negated);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        long sum1 = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, sum1);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method with long.
     */
    @Test
    public void testSafeMultiplyLong() {
        long val1 = 5;
        int val2 = 3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, product);

        val1 = Long.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiply method with negative values.
     */
    @Test
    public void testSafeMultiplyNegative() {
        long val1 = 5;
        long val2 = -3;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15L, product);

        val1 = Long.MIN_VALUE;
        val2 = -1;
        long product1 = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-Long.MIN_VALUE, product1);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeToInt method.
     */
    @Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, intValue);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, product);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test verifyValueBounds method.
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.hourOfDay();

        int value = 13;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
        } catch (IllegalArgumentException e) {
            fail();
        }

        value = 24;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        value = 1;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 0, 23);
        } catch (IllegalArgumentException e) {
            fail();
        }

        value = 25;
        DateTimeField field = new DateTimeField(fieldType, "test", 0);
        try {
            FieldUtils.verifyValueBounds(field, value, 0, 23);
            fail();
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    /**
     * Test getWrappedValue method.
     */
    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;

        int value1 = 0;
        int wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = minValue;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = maxValue;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = maxValue + 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = Integer.MIN_VALUE;
        try {
            FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        value1 = minValue - 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(maxValue, wrappedValue1);

        value1 = maxValue - 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = Integer.MAX_VALUE;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);

        value1 = minValue + 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, -1, minValue, maxValue);
        assertEquals(maxValue, wrappedValue1);

        value1 = maxValue + 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, -1, minValue, maxValue);
        assertEquals(minValue, wrappedValue1);
    }
}