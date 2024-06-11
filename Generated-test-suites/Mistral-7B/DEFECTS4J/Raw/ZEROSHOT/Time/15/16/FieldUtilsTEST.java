package org.joda.time.test.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int negated = FieldUtils.safeNegate(value);
        assertEquals(-5, negated);
    }

    @org.junit.Test
    public void testSafeNegateMinValue() {
        int value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 1;
        int val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);
    }

    @org.junit.Test
    public void testSafeAddOverflow() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @org.junit.Test
    public void testSafeAddSameSign() {
        int val1 = Integer.MIN_VALUE;
        int val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @org.junit.Test
    public void testSafeAddSameValue() {
        int val1 = 5;
        int val2 = 5;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(10, sum);
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, diff);
    }

    @org.junit.Test
    public void testSafeSubtractOverflow() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @org.junit.Test
    public void testSafeSubtractDifferentSigns() {
        long val1 = Long.MIN_VALUE;
        long val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, product);
    }

    @org.junit.Test
    public void testSafeMultiplyOverflowInt() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiplyOverflowLong() {
        long val1 = Long.MIN_VALUE;
        long val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiplyLongToInt() {
        long val1 = 5L;
        int val2 = 3;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, product);
    }

    @org.junit.Test
    public void testSafeMultiplyLongToIntOverflow() {
        long val1 = Long.MIN_VALUE;
        int val2 = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test");
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;
        FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfMonth";
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }

    @org.junit.Test
    public void testVerifyValueBoundsOutOfBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test");
        int value = 32;
        int lowerBound = 1;
        int upperBound = 30;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 3;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(5, wrappedValue);
    }

    @org.junit.Test
    public void testGetWrappedValueMinGreaterThanMax() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(0, 5, 4));
    }

    @org.junit.Test
    public void testGetWrappedValueNegativeValue() {
        int value = -1;
        int minValue = 0;
        int maxValue = 3;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(3, wrappedValue);
    }

    @org.junit.Test
    public void testGetWrappedValueValueOutOfRange() {
        int value = 4;
        int minValue = 0;
        int maxValue = 3;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);
    }

    @org.junit.Test
    public void testGetWrappedValueValueInRange() {
        int value = 1;
        int minValue = 0;
        int maxValue = 3;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(1, wrappedValue);
    }
}