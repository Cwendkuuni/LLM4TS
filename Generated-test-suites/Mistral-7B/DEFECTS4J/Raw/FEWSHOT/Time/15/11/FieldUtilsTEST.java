package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(-2147483648, FieldUtils.safeNegate(Integer.MIN_VALUE));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertEquals(1023, FieldUtils.safeAdd(1023, 1));
        assertEquals(Integer.MAX_VALUE, FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertEquals(-2, FieldUtils.safeAdd(1, -3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertEquals(-1, FieldUtils.safeSubtract(2, 3));
        assertEquals(-2147483648, FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(1, FieldUtils.safeMultiply(1, 1));
        assertEquals(5, FieldUtils.safeMultiply(1, 5));
        assertEquals(-1, FieldUtils.safeMultiply(-1, 1));
        assertEquals(-5, FieldUtils.safeMultiply(-1, 5));
        assertEquals(Integer.MIN_VALUE, FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, Integer.MIN_VALUE + 1));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long result;

        result = FieldUtils.safeMultiply(1, 5);
        assertEquals(5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, 5);
        assertEquals(-5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, -5);
        assertEquals(5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, -5);
        assertEquals(-5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, 1);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE + 1);
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(result));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long result;

        result = FieldUtils.safeMultiply(1, 5L);
        assertEquals(5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, 5L);
        assertEquals(-5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, -5L);
        assertEquals(5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(-1, -5L);
        assertEquals(-5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, 1L);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MIN_VALUE + 1L);
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(result));
    }

    @Test
    public void testSafeAddLongInt() {
        long result;

        result = FieldUtils.safeAdd(0L, 5);
        assertEquals(5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeAdd(Long.MIN_VALUE, 5);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeAdd(Long.MIN_VALUE, -5);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeAdd(Long.MIN_VALUE, -5);
        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void testSafeSubtractLongInt() {
        long result;

        result = FieldUtils.safeSubtract(0L, 5);
        assertEquals(-5, FieldUtils.safeToInt(result));

        result = FieldUtils.safeSubtract(Long.MIN_VALUE, 5);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeSubtract(Long.MIN_VALUE, -5);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeSubtract(Long.MIN_VALUE, -5);
        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void testSafeMultiplyLongLongToInt() {
        int result;

        result = FieldUtils.safeMultiplyToInt(1, 5L);
        assertEquals(5, result);

        result = FieldUtils.safeMultiplyToInt(-1, 5L);
        assertEquals(-5, result);

        result = FieldUtils.safeMultiplyToInt(-1, -5L);
        assertEquals(5, result);

        result = FieldUtils.safeMultiplyToInt(-1, -5L);
        assertEquals(-5, result);

        result = FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, 1L);
        assertEquals(Long.MIN_VALUE, result);

        result = FieldUtils.safeMultiplyToInt(Long.MIN_VALUE, Long.MIN_VALUE + 1L);
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(result));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.DAY_OF_MONTH, 0, null);
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, field.getType());

        int lowerBound = 1;
        int upperBound = 10;

        // Valid values
        for (int i = 1; i <= 10; i++) {
            FieldUtils.verifyValueBounds(field, i, lowerBound, upperBound);
        }

        // Lower bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, lowerBound, upperBound));

        // Upper bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 11, lowerBound, upperBound));

        // Lower and upper bounds
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 0, 11, 10));
    }

    @Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.DAY_OF_MONTH;
        assertEquals(DateTimeFieldType.DAY_OF_MONTH, fieldType);

        int lowerBound = 1;
        int upperBound = 10;

        // Valid values
        for (int i = 1; i <= 10; i++) {
            FieldUtils.verifyValueBounds(fieldType, i, lowerBound, upperBound);
        }

        // Lower bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, lowerBound, upperBound));

        // Upper bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 11, lowerBound, upperBound));

        // Lower and upper bounds
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 0, 11, 10));
    }

    @Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";

        int lowerBound = 1;
        int upperBound = 10;

        // Valid values
        for (int i = 1; i <= 10; i++) {
            FieldUtils.verifyValueBounds(fieldName, i, lowerBound, upperBound);
        }

        // Lower bound
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, lowerBound, upperBound));

        // Upper bound
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, 11, lowerBound, upperBound));

        // Lower and upper bounds
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, 0, 11, 10));
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 1;
        int maxValue = 5;
        int currentValue;
        int wrapValue = 6;

        // Within range
        currentValue = 3;
        assertEquals(3, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Boundary value, positive wrap
        currentValue = minValue;
        assertEquals(minValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Boundary value, negative wrap
        currentValue = maxValue;
        assertEquals(maxValue, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Valid value, negative wrap
        currentValue = 2;
        assertEquals(1, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Value outside range
        currentValue = -1;
        assertEquals(5, FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue));

        // Valid value, wrap equals range size
        wrapValue = 6;
        assertEquals(1, FieldUtils.getWrappedValue(1, wrapValue, minValue, maxValue));
        assertEquals(5, FieldUtils.getWrappedValue(5, wrapValue, minValue, maxValue));
        assertEquals(1, FieldUtils.getWrappedValue(minValue, wrapValue, minValue, maxValue));
        assertEquals(5, FieldUtils.getWrappedValue(maxValue, wrapValue, minValue, maxValue));

        // Valid value, wrap less than range size
        wrapValue = 4;
        assertEquals(2, FieldUtils.getWrappedValue(1, wrapValue, minValue, maxValue));
        assertEquals(4, FieldUtils.getWrappedValue(2, wrapValue, minValue, maxValue));
        assertEquals(1, FieldUtils.getWrappedValue(5, wrapValue, minValue, maxValue));
        assertEquals(1, FieldUtils.getWrappedValue(minValue, wrapValue, minValue, maxValue));
        assertEquals(4, FieldUtils.getWrappedValue(maxValue, wrapValue, minValue, maxValue));

        // Value equals range minimum and wrap value is less than range size
        wrapValue = 3;
        assertEquals(1, FieldUtils.getWrappedValue(minValue, wrapValue, minValue, maxValue));

        // Value equals range maximum and wrap value is greater than range size
        wrapValue = 7;
        assertEquals(5, FieldUtils.getWrappedValue(maxValue, wrapValue, minValue, maxValue));

        // Value equals range minimum and wrap value is greater than range size
        wrapValue = 8;
        assertEquals(1, FieldUtils.getWrappedValue(minValue, wrapValue, minValue, maxValue));

        // Value equals range maximum and wrap value is less than range size
        wrapValue = 4;
        assertEquals(5, FieldUtils.getWrappedValue(maxValue, wrapValue, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;

        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj3));
        assertTrue(FieldUtils.equals(obj3, obj1));

        assertFalse(FieldUtils.equals(obj1, obj2));
        assertFalse(FieldUtils.equals(obj2, obj1));
        assertFalse(FieldUtils.equals(obj2, obj3));

        assertFalse(FieldUtils.equals(null, obj1));
        assertFalse(FieldUtils.equals(obj1, null));
    }
}