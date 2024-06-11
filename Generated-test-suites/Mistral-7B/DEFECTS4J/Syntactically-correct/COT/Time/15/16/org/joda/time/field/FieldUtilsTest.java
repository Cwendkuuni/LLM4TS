package org.joda.time.field;

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
            // Expected exception
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
            // Expected exception
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
            // Expected exception
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        try {
            result = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
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
            // Expected exception
        }
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15L, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            result = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
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
            // Expected exception
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 5L;
        int result = FieldUtils.safeToInt(value);
        assertEquals(5, result);

        value = Long.MIN_VALUE + 1; // one below MIN_VALUE is allowed
        result = FieldUtils.safeToInt(value);
        assertEquals(Integer.MIN_VALUE, result);

        value = Long.MIN_VALUE;
        try {
            result = FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
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
            // Expected exception
        }
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 28, new GregorianCalendar());
        int value = 28;
        int lowerBound = 1;
        int upperBound = 31;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 32;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        // Test with different types of field
        DateTimeFieldType fieldType = DateTimeFieldType.millisecondOfMinute();
        value = 60001;
        lowerBound = 0;
        upperBound = 60000;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        value = 60002;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        String fieldName = "testField";
        value = 30;
        lowerBound = 1;
        upperBound = 29;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }

        value = 31;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetWrappedValue() {
        int value = 10;
        int minValue = 1;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, result);

        value = Integer.MIN_VALUE;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
        try {
            result = FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        minValue = maxValue;
        try {
            result = FieldUtils.getWrappedValue(value, minValue, maxValue);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        value = -1;
        minValue = 1;
        maxValue = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, result);

        value = 6;
        minValue = 1;
        maxValue = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }

    
}