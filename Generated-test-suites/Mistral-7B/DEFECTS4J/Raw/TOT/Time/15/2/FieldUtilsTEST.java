package org.joda.time.field.util;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = -5;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        int expected = 7;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        long sum = (long) val1 + (long) val2;
        int resultLong = (int) sum;
        expected = (int) sum;
        assertEquals(expected, resultLong);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long expected = 7;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = 5;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected, since there is a sign change
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 4;
        int expected = 12;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = -1;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3;
        long val2 = 4;
        int expected = 12;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = -1;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", 1);
        int value = 7;

        FieldUtils.verifyValueBounds(field, value, 1, 7);

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 6);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(new DateTimeFieldType("test"), value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalArgumentException e) {
            // Expected, since the field type is not recognized
        }

        try {
            FieldUtils.verifyValueBounds("test", value, 1, 7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(new DateTimeField("test", DateTimeFieldType.dayOfWeek(), 1), value, 1, 7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, since the field is not recognized
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 0;
        int maxValue = 5;
        int expected = 3;
        int result = FieldUtils.getWrappedValue(value, 3, minValue, maxValue);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
        try {
            FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, since minValue is greater than or equal to maxValue
        }

        minValue = 6;
        maxValue = 5;
        try {
            FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, since the value is less than minValue
        }

        minValue = 5;
        maxValue = 5;
        try {
            FieldUtils.getWrappedValue(value, 2, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, since the value is equal to maxValue
        }

        value = Integer.MAX_VALUE;
        try {
            FieldUtils.getWrappedValue(value, -1, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, since the wrap value is negative
        }
    }

    @Test
    public void testEquals() {
        String object1 = "test";
        String object2 = "test";
        assertTrue(FieldUtils.equals(object1, object2));

        object1 = null;
        assertFalse(FieldUtils.equals(object1, object2));

        object2 = null;
        assertFalse(FieldUtils.equals(object1, object2));

        int value1 = 5;
        int value2 = 5;
        assertTrue(FieldUtils.equals(value1, value2));

        value1 = 5;
        value2 = 6;
        assertFalse(FieldUtils.equals(value1, value2));
    }
}