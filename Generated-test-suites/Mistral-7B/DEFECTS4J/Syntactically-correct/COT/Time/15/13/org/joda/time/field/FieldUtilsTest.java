package org.joda.time.field; // Import necessary packages if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int input = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(input);
        assertEquals(expected, result);

        input = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(input));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        int expected = 8;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        expected = Integer.MIN_VALUE;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long expected = 7L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = 1L;
        val2 = 1L;
        expected = 0L;
        result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int expected = 15;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long expected = 15L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long expected = 15L;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeToInt() {
        long input = 5L;
        int expected = 5;
        int result = FieldUtils.safeToInt(input);
        assertEquals(expected, result);

        input = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(input));

        input = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(input));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int expected = 15;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MIN_VALUE;
        val2 = 2L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @Test
    public void testVerifyValueBoundsIntIntInt() {
        DateTimeField field = null;
        int value = 5;
        int lowerBound = 1;
        int upperBound = 10;

        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        value = 15;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("Value is out of bounds: 15, lower bound: 1, upper bound: 10", e.getMessage());
        }

        value = 1;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldIntIntInt() {
        DateTimeField field = DateTimeField.forIntegerMillis(1);
        int value = 5;
        int lowerBound = 1;
        int upperBound = 10;

        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        value = 15;
        try {
            FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("Value is out of bounds: 15, lower bound: 1, upper bound: 10", e.getMessage());
        }

        value = 1;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldTypeIntIntInt() {
        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        int value = 5;
        int lowerBound = 1;
        int upperBound = 10;

        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        value = 15;
        try {
            FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("Value is out of bounds: 15, lower bound: 1, upper bound: 10", e.getMessage());
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @Test
    public void testVerifyValueBoundsStringIntIntInt() {
        String fieldName = "test";
        int value = 5;
        int lowerBound = 1;
        int upperBound = 10;

        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        value = 15;
        try {
            FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
        } catch (IllegalFieldValueException e) {
            assertEquals("Value is out of bounds: 15, lower bound: 1, upper bound: 10", e.getMessage());
        }

        value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValueIntIntInt() {
        int currentValue = 15;
        int wrapValue = 5;
        int minValue = 0;
        int maxValue = 10;
        int expected = 2;
        int result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        currentValue = -15;
        wrapValue = -5;
        minValue = 0;
        maxValue = 10;
        expected = 6;
        result = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        assertEquals(expected, result);

        currentValue = Integer.MIN_VALUE;
        wrapValue = 5;
        minValue = 0;
        maxValue = 10;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        String object1 = "String1";
        String object2 = "String2";
        boolean expected = false;
        boolean result = FieldUtils.equals(object1, object2);
        assertEquals(expected, result);

        Object object3 = new Object();
        expected = false;
        result = FieldUtils.equals(object1, object3);
        assertEquals(expected, result);

        object3 = object1;
        expected = true;
        result = FieldUtils.equals(object1, object3);
        assertEquals(expected, result);

        object1 = null;
        expected = false;
        result = FieldUtils.equals(object1, object2);
        assertEquals(expected, result);

        object1 = object3;
        expected = true;
        result = FieldUtils.equals(object1, null);
        assertEquals(expected, result);
    }

    
}