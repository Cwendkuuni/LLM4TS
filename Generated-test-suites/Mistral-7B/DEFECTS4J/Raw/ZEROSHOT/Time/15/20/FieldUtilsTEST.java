package org.joda.time.test.field;

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        int expected = -5;
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        int expected = 8;
        assertEquals(expected, result);

        val1 = 100;
        val2 = -5;
        result = FieldUtils.safeAdd(val1, val2);
        expected = 99;
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = FieldUtils.safeSubtract(val1, val2);
        long expected = 7L;
        assertEquals(expected, result);

        val1 = 10L;
        val2 = 10L;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        int expected = 15;
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        long expected = 15L;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long result = FieldUtils.safeMultiply(val1, val2);
        long expected = 15L;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int result = FieldUtils.safeToInt(value);
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        int expected = 15;
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "DOW");
        int value = 32;
        int lowerBound = 1;
        int upperBound = 31;

        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 30;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 29;
        DateTimeFieldType fieldType = DateTimeFieldType.monthOfYear();
        FieldUtils.verifyValueBounds(fieldType, value, 1, 12);

        String fieldName = "DOW";
        value = 33;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 10;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 14;
        int result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        int expected = 5;
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        minValue = 1;
        maxValue = 5;
        try {
            result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        wrapValue = -1;
        minValue = 1;
        maxValue = 5;
        try {
            result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        wrapValue = 5;
        minValue = 1;
        maxValue = 1;
        try {
            result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        wrapValue = 5;
        minValue = 1;
        maxValue = 6;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        expected = 6;
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        wrapValue = 5;
        minValue = 1;
        maxValue = 7;
        result = FieldUtils.getWrappedValue(value, wrapValue, minValue, maxValue);
        expected = 1;
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = FieldUtils.equals(obj1, obj2);
        boolean expected = false;
        assertEquals(expected, result);

        obj1 = obj2;
        result = FieldUtils.equals(obj1, obj2);
        expected = true;
        assertEquals(expected, result);

        obj1 = null;
        result = FieldUtils.equals(obj1, obj2);
        expected = false;
        assertEquals(expected, result);

        obj2 = null;
        result = FieldUtils.equals(obj1, obj2);
        expected = false;
        assertEquals(expected, result);

        obj1 = new String("test");
        obj2 = new String("test");
        result = FieldUtils.equals(obj1, obj2);
        expected = true;
        assertEquals(expected, result);
    }
}