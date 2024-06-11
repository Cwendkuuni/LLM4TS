package org.joda.time.field.test;

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.joda.time.field.FieldUtils;

import org.junit.Test;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    /**
     * Test method for {@link FieldUtils#safeNegate(int)}.
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        int expected = -value;
        assertEquals("safeNegate(5) did not return -5", expected, result);
        
        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeAdd(int, int)}.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        int expected = val1 + val2;
        assertEquals("safeAdd(5, 3) did not return 8", expected, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        expected = val1 + val2;
        assertEquals("safeAdd(Integer.MIN_VALUE, 1) did not return -6", expected, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeSubtract(long, long)}.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        long expected = val1 - val2;
        assertEquals("safeSubtract(10, 3) did not return 7", expected, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(int, int)}.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int total = FieldUtils.safeMultiply(val1, val2);
        int expected = val1 * val2;
        assertEquals("safeMultiply(5, 3) did not return 15", expected, total);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(long, int)}.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long total = FieldUtils.safeMultiply(val1, val2);
        long expected = val1 * val2;
        assertEquals("safeMultiply(5, 3) did not return 15", expected, total);

        val1 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = 5L;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeMultiply(long, long)}.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long total = FieldUtils.safeMultiply(val1, val2);
        long expected = val1 * val2;
        assertEquals("safeMultiply(5, 3) did not return 15", expected, total);

        val1 = Long.MIN_VALUE;
        val2 = -1L;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeToInt(long)}.
     */
    @Test
    public void testSafeToInt() {
        long value = 5L;
        int result = FieldUtils.safeToInt(value);
        int expected = (int) value;
        assertEquals("safeToInt(5) did not return 5", expected, result);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#safeMultiplyToInt(long, long)}.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        int expected = (int) (val1 * val2);
        assertEquals("safeMultiplyToInt(5, 3) did not return 15", expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(DateTimeField, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsDateTimeField() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test");
        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        FieldUtils.verifyValueBounds(field, 1, 1, 31);
        FieldUtils.verifyValueBounds(field, 31, 1, 31);
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(DateTimeFieldType, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        FieldUtils.verifyValueBounds(fieldType, 1, 1, 31);
        FieldUtils.verifyValueBounds(fieldType, 31, 1, 31);
    }

    /**
     * Test method for {@link FieldUtils#verifyValueBounds(String, int, int, int)}.
     */
    @Test
    public void testVerifyValueBoundsString() {
        try {
            FieldUtils.verifyValueBounds("test", 32, 1, 31);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        FieldUtils.verifyValueBounds("test", 1, 1, 31);
        FieldUtils.verifyValueBounds("test", 31, 1, 31);
    }

    /**
     * Test method for {@link FieldUtils#getWrappedValue(int, int, int, int)}.
     */
    @Test
    public void testGetWrappedValue() {
        int value = 12;
        int minValue = 0;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, Integer.MIN_VALUE, minValue, maxValue);
        int expected = value % (maxValue - minValue + 1) + minValue;
        assertEquals("getWrappedValue(12, Integer.MIN_VALUE, 0, 5) did not return 2", expected, result);

        value = Integer.MIN_VALUE + 1;
        result = FieldUtils.getWrappedValue(value, minValue, minValue, maxValue);
        expected = minValue;
        assertEquals("getWrappedValue(Integer.MIN_VALUE+1, 0, 0, 5) did not return 0", expected, result);

        value = Integer.MIN_VALUE - 1;
        result = FieldUtils.getWrappedValue(value, minValue, minValue, maxValue);
        expected = maxValue;
        assertEquals("getWrappedValue(Integer.MIN_VALUE-1, 0, 0, 5) did not return 5", expected, result);

        minValue = maxValue;
        try {
            FieldUtils.getWrappedValue(value, minValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link FieldUtils#equals(Object, Object)}.
     */
    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse("equals(null, null) should be false", FieldUtils.equals(null, null));
        assertFalse("equals(null, obj1) should be false", FieldUtils.equals(null, obj1));
        assertFalse("equals(obj1, null) should be false", FieldUtils.equals(obj1, null));
        assertFalse("equals(obj1, obj2) should be false", FieldUtils.equals(obj1, obj2));

        obj1 = "test";
        obj2 = "test";
        assertTrue("equals(obj1, obj2) should be true", FieldUtils.equals(obj1, obj2));
    }
}