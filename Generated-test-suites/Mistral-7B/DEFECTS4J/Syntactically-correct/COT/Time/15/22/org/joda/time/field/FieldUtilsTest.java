package org.joda.time.field;

import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.field.FieldUtils.safeAdd;
import static org.joda.time.field.FieldUtils.safeNegate;
import static org.joda.time.field.FieldUtils.safeMultiply;
import static org.joda.time.field.FieldUtils.safeSubtract;

/**
 * Test class for FieldUtils.
 *
 * @author John Doe
 * @since 1.0
 */
public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = Integer.MIN_VALUE;
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
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, diff);

        val1 = Long.MIN_VALUE;
        val2 = 5L;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 10L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 3;
        int val2 = 2;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply_long_int() {
        long val1 = 3L;
        int val2 = 2;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, product);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3L;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiply_long_long() {
        long val1 = 3L;
        long val2 = 2L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6L, product);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeToInt() {
        long value = 10L;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(10, intValue);

        value = Integer.MIN_VALUE + 1L;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 3L;
        long val2 = 2L;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(6, product);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 3L;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyValueBounds_minValueGreaterThanMaxValue() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(fieldType, 1, 2, 1);
        // This should throw IllegalArgumentException
    }

    @Test
    public void testVerifyValueBounds_int() {
        DateTimeField field = DateTimeField.forIntegerFields(DateTimeFieldType.dayOfMonth());
        FieldUtils.verifyValueBounds(field, 15, 1, 31);

        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        FieldUtils.verifyValueBounds(fieldType, 15, 1, 31);

        String fieldName = "dayOfMonth";
        FieldUtils.verifyValueBounds(fieldName, 15, 1, 31);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBounds_outOfBounds() {
        DateTimeField field = DateTimeField.forIntegerFields(DateTimeFieldType.dayOfMonth());
        FieldUtils.verifyValueBounds(field, 33, 1, 31);
        // This should throw IllegalFieldValueException
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        value = -2;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, wrappedValue);

        value = -7;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(2, wrappedValue);

        minValue = 2;
        maxValue = 5;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);

        minValue = 1;
        maxValue = 0;
        try {
            FieldUtils.getWrappedValue(value, minValue, maxValue);
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

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj2 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = new String("Hello");
        obj2 = new String("Hello");
        assertTrue(FieldUtils.equals(obj1, obj2));
    }

    
}