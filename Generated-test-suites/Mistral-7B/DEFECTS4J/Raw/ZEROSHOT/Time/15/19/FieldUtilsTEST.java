package org.joda.time.test.field;

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

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(7, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(Integer.MIN_VALUE, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
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
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = 5L;
        val2 = -3;
        long result1 = FieldUtils.safeMultiply(val1, val2);
        assertEquals(-15L, result1);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
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
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 123L;
        int result = FieldUtils.safeToInt(value);
        assertEquals(123, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
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
            // Expected
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MILLISECONDS, "test");

        int value = 10;
        try {
            FieldUtils.verifyValueBounds(field, value, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 15;
        FieldUtils.verifyValueBounds(field, value, 5, 15);

        try {
            FieldUtils.verifyValueBounds(field, 0, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 20, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.MILLISECONDS;

        int value = 10;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 15;
        FieldUtils.verifyValueBounds(fieldType, value, 5, 15);

        try {
            FieldUtils.verifyValueBounds(fieldType, 0, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 20, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "test";

        int value = 10;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        value = 15;
        FieldUtils.verifyValueBounds(fieldName, value, 5, 15);

        try {
            FieldUtils.verifyValueBounds(fieldName, 0, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 20, 5, 15);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(10, wrappedValue);

        value = -5;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);

        minValue = 5;
        maxValue = 0;
        try {
            wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}