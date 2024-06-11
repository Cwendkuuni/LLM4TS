package org.joda.time.test.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 3;
        int val2 = 4;
        int result = FieldUtils.safeAdd(val1, val2);
        assertEquals(7, result);

        long longVal1 = 100L;
        long longVal2 = 200L;
        long resultLong = FieldUtils.safeAdd(longVal1, longVal2);
        assertEquals(300L, resultLong);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        longVal1 = Long.MIN_VALUE;
        longVal2 = 1L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(longVal1, longVal2));
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 100L;
        long val2 = 20L;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(80L, result);

        val1 = Long.MIN_VALUE;
        val2 = 10L;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, result);

        long longVal1 = 100L;
        int val3 = 5;
        long resultLong = FieldUtils.safeMultiply(longVal1, val3);
        assertEquals(500L, resultLong);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        longVal1 = Long.MIN_VALUE;
        val3 = 5;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(longVal1, val3));
    }

    @org.junit.Test
    public void testSafeMultiplyLongToInt() {
        long val1 = 10L;
        int val2 = 5;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(50, result);

        val1 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 31;

        FieldUtils.verifyValueBounds(field, value, 1, 31);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 1, 31));
    }

    @org.junit.Test
    public void testVerifyValueBoundsInt() {
        int lowerBound = 1;
        int upperBound = 31;
        int value = 31;

        FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), value, lowerBound, upperBound);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfMonth(), value, lowerBound, upperBound));
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";
        int value = 31;

        FieldUtils.verifyValueBounds(fieldName, value, 1, 31);

        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 1, 31));
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, result);

        value = 6;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);

        minValue = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, result);

        maxValue = 4;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, result);

        minValue = 5;
        maxValue = 4;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }

    @org.junit.Test
    public void testEquals() {
        Object obj1 = "test";
        Object obj2 = "test";
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj2 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        assertFalse(FieldUtils.equals(obj1, obj2));
    }
}