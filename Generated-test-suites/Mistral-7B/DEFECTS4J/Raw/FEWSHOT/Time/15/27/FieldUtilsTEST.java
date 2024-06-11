package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-2));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(1, FieldUtils.safeAdd(0, 1));
        assertEquals(5, FieldUtils.safeAdd(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd_long() {
        assertEquals(1L, FieldUtils.safeAdd(0L, 1L));
        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        assertEquals(3, FieldUtils.safeSubtract(5, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(0, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(2, 0));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_int_long() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(3, FieldUtils.safeMultiply(1, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_long_int() {
        assertEquals(0, FieldUtils.safeMultiply(0L, 0));
        assertEquals(3L, FieldUtils.safeMultiply(1L, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply_long_long() {
        assertEquals(0, FieldUtils.safeMultiply(0L, 0L));
        assertEquals(9L, FieldUtils.safeMultiply(3L, 3L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(1L, Long.MIN_VALUE));
    }

    @Test
    public void testSafeToInt() {
        assertEquals(0, FieldUtils.safeToInt(0));
        assertEquals(-1, FieldUtils.safeToInt(Integer.MIN_VALUE + 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiplyToInt() {
        assertEquals(0, FieldUtils.safeMultiplyToInt(0, 0));
        assertEquals(-3, FieldUtils.safeMultiplyToInt(1, -3));
        assertEquals(2, FieldUtils.safeMultiplyToInt(3, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(1, Integer.MIN_VALUE));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.MILLISECONDS, "test");
        int lowerBound = 0;
        int upperBound = 5;

        // Within bounds
        FieldUtils.verifyValueBounds(field, 3, lowerBound, upperBound);

        // Below lower bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 2, lowerBound, upperBound));

        // Above upper bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 6, lowerBound, upperBound));
    }

    @Test
    public void testVerifyValueBounds_DateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.MILLISECONDS;
        int lowerBound = 0;
        int upperBound = 5;

        // Within bounds
        FieldUtils.verifyValueBounds(fieldType, 3, lowerBound, upperBound);

        // Below lower bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 2, lowerBound, upperBound));

        // Above upper bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldType, 6, lowerBound, upperBound));
    }

    @Test
    public void testVerifyValueBounds_String() {
        String fieldName = "test";
        int lowerBound = 0;
        int upperBound = 5;

        // Within bounds
        FieldUtils.verifyValueBounds(fieldName, 3, lowerBound, upperBound);

        // Below lower bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 2, lowerBound, upperBound));

        // Above upper bound
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, 6, lowerBound, upperBound));
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;

        // Within wrap range
        assertEquals(3, FieldUtils.getWrappedValue(3, minValue, maxValue));
        assertEquals(1, FieldUtils.getWrappedValue(minValue + 1, minValue, maxValue));
        assertEquals(6, FieldUtils.getWrappedValue(maxValue - 1, minValue, maxValue));

        // Below min value
        assertEquals(minValue, FieldUtils.getWrappedValue(minValue - 1, minValue, maxValue));

        // Above max value
        assertEquals(maxValue, FieldUtils.getWrappedValue(maxValue + 1, minValue, maxValue));

        // Min value equals max value
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(minValue, minValue, minValue));

        // Negative values
        assertEquals(-2, FieldUtils.getWrappedValue(-2, minValue, maxValue));
        assertEquals(5, FieldUtils.getWrappedValue(-3, minValue, maxValue));

        // Large negative value
        assertEquals(minValue, FieldUtils.getWrappedValue(Integer.MIN_VALUE, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = obj1;
        String str1 = "test";
        String str2 = "test";

        // Equal objects
        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj3));

        // Null objects
        assertFalse(FieldUtils.equals(null, obj1));
        assertFalse(FieldUtils.equals(obj1, null));

        // Non-equal objects
        assertFalse(FieldUtils.equals(obj1, obj2));

        // Equal strings
        assertTrue(FieldUtils.equals(str1, str1));
        assertTrue(FieldUtils.equals(str1, str2));

        // Null strings
        assertFalse(FieldUtils.equals(null, str1));
        assertFalse(FieldUtils.equals(str1, null));
    }
}