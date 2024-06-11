package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertFalse(FieldUtils.safeNegate(0) < 0);
        assertFalse(FieldUtils.safeNegate(1) < 0);
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        int a = 1;
        int b = 2;
        int sum = FieldUtils.safeAdd(a, b);
        assertTrue(sum == 3);

        a = Integer.MIN_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MIN_VALUE;
        b = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MAX_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MAX_VALUE;
        b = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));
    }

    @Test
    public void testSafeAddWithNegative() {
        int a = 1;
        int b = -1;
        int sum = FieldUtils.safeAdd(a, b);
        assertTrue(sum == 0);

        a = Integer.MIN_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MIN_VALUE;
        b = -1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MAX_VALUE;
        b = -1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));

        a = Integer.MAX_VALUE;
        b = -1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(a, b));
    }

    @Test
    public void testSafeSubtract() {
        int a = 3;
        int b = 1;
        int diff = FieldUtils.safeSubtract(a, b);
        assertTrue(diff == 2);

        a = Integer.MIN_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(a, b));

        a = Integer.MIN_VALUE;
        b = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(a, b));

        a = Integer.MAX_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(a, b));

        a = Integer.MAX_VALUE;
        b = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(a, b));
    }

    @Test
    public void testSafeMultiply() {
        int a = 2;
        int b = 3;
        int product = FieldUtils.safeMultiply(a, b);
        assertTrue(product == 6);

        a = Integer.MIN_VALUE;
        b = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));
    }

    @Test
    public void testSafeMultiplyWithNegative() {
        int a = 2;
        int b = -3;
        long product = FieldUtils.safeMultiply(a, b);
        assertTrue(product == -6);

        a = Integer.MIN_VALUE;
        b = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));

        a = Integer.MIN_VALUE;
        b = -1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));

        a = Integer.MAX_VALUE;
        b = -1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));
    }

    @Test
    public void testSafeMultiplyWithLong() {
        int a = 2;
        long b = 3;
        long product = FieldUtils.safeMultiply(a, b);
        assertTrue(product == 6L);

        a = Integer.MIN_VALUE;
        b = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));
    }

    @Test
    public void testSafeMultiplyLongWithInt() {
        long a = 2;
        int b = 3;
        long product = FieldUtils.safeMultiply(a, b);
        assertTrue(product == 6L);
    }

    @Test
    public void testSafeMultiplyLongWithLong() {
        long a = 2;
        long b = 3;
        long product = FieldUtils.safeMultiply(a, b);
        assertTrue(product == 6L);
    }

    @Test
    public void testSafeToInt() {
        long a = 123;
        int b = FieldUtils.safeToInt(a);
        assertTrue(b == 123);

        a = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(a));

        a = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(a));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long a = 2;
        long b = 3;
        int result = FieldUtils.safeMultiplyToInt(a, b);
        assertTrue(result == 6);

        a = 1;
        b = 2;
        result = FieldUtils.safeMultiplyToInt(a, b);
        assertTrue(result == 2);

        a = Long.MAX_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(a, b));

        a = Long.MIN_VALUE;
        b = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(a, b));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth", null);

        int minValue = 1;
        int maxValue = 31;
        int value = 32;

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, minValue, maxValue));

        value = 31;
        FieldUtils.verifyValueBounds(field, value, minValue, maxValue);

        value = 1;
        FieldUtils.verifyValueBounds(field, value, minValue, maxValue);

        value = 0;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, minValue, maxValue));

        minValue = 0;
        maxValue = 31;
        value = 32;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, minValue, maxValue));

        minValue = 32;
        maxValue = 31;
        value = 31;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, minValue, maxValue));

        minValue = 32;
        maxValue = 32;
        value = 33;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, minValue, maxValue));
    }

    @Test
    public void testVerifyValueBoundsWithNullField() {
        DateTimeField field = null;
        int value = 1;
        int lowerBound = 1;
        int upperBound = 2;
        assertThrows(NullPointerException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));
    }

    @Test
    public void testVerifyValueBoundsWithNullFieldType() {
        DateTimeFieldType fieldType = null;
        int value = 1;
        int lowerBound = 1;
        int upperBound = 2;
        assertThrows(NullPointerException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, lowerBound, upperBound));
    }

    @Test
    public void testVerifyValueBoundsWithNullString() {
        String fieldName = null;
        int value = 1;
        int lowerBound = 1;
        int upperBound = 2;
        assertThrows(NullPointerException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound));
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertTrue(wrappedValue == value);

        value = 6;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertTrue(wrappedValue == (value % (maxValue - minValue + 1)) + minValue);

        value = -1;
        wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertTrue(wrappedValue == (maxValue - (value % (maxValue - minValue + 1))) + minValue);
    }

    @Test
    public void testGetWrappedValueWithMinMaxNotSorted() {
        int value = 5;
        int minValue = 5;
        int maxValue = 0;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }
}