package org.joda.time.field.test;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);
        value = -5;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, result);
        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 5L;
        long val2 = 3L;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(8L, result);
        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(7L, result);
        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
        val1 = 5L;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);
        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);
        val1 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);
        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 5L;
        int result = FieldUtils.safeToInt(value);
        assertEquals(5, result);
        value = Long.MAX_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, result);
        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            result = FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 1, 1, 31);
        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            FieldUtils.verifyValueBounds(fieldType, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        String fieldName = "dayOfMonth";
        try {
            FieldUtils.verifyValueBounds(fieldName, 33, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 5;
        int maxValue = 15;
        int result = FieldUtils.getWrappedValue(value, -3, minValue, maxValue);
        assertEquals(12, result);
        value = Integer.MIN_VALUE;
        minValue = 1;
        maxValue = Integer.MAX_VALUE;
        result = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(Integer.MIN_VALUE, result);
        minValue = maxValue;
        try {
            result = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));
        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));
    }
}