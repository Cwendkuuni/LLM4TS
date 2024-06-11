package org.joda.time.field; // Make sure the test class is in the same package as the FieldUtils class

import org.junit.Test;
import org.junit.Assert.*;

import static org.joda.time.field.FieldUtils.*;

public class FieldUtilsTest {

    

    @Test
    public void testSafeNegateInt() {
        int value = 5;
        int result = safeNegate(value);
        int expected = -5;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinInt() {
        int value = Integer.MIN_VALUE;
        safeNegate(value);
    }

    @Test
    public void testSafeAddIntInt() {
        int val1 = 3;
        int val2 = 4;
        int result = safeAdd(val1, val2);
        int expected = 7;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddIntOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 1;
        safeAdd(val1, val2);
    }

    @Test
    public void testSafeAddLongInt() {
        long val1 = 3L;
        int val2 = 4;
        long result = safeAdd(val1, val2);
        long expected = 7L;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeAddLongOverflow() {
        long val1 = Long.MAX_VALUE;
        int val2 = 1;
        safeAdd(val1, val2);
    }

    @Test
    public void testSafeSubtractLongLong() {
        long val1 = 10L;
        long val2 = 5L;
        long result = safeSubtract(val1, val2);
        long expected = 5L;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractLongUnderflow() {
        long val1 = -5L;
        long val2 = 5L;
        safeSubtract(val1, val2);
    }

    @Test
    public void testSafeMultiplyIntInt() {
        int val1 = 3;
        int val2 = 4;
        int result = safeMultiply(val1, val2);
        int expected = 12;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyIntOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 2;
        safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 3L;
        int val2 = 4;
        long result = safeMultiply(val1, val2);
        long expected = 12L;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongOverflow() {
        long val1 = Long.MAX_VALUE;
        int val2 = 2;
        safeMultiply(val1, val2);
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long result = safeMultiply(val1, val2);
        long expected = 15L;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongUnderflow() {
        long val1 = -5L;
        long val2 = 3L;
        safeMultiply(val1, val2);
    }

    @Test
    public void testSafeToIntLong() {
        long value = 123;
        int result = safeToInt(value);
        int expected = 123;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeToIntLongOverflow() {
        long value = Long.MAX_VALUE + 1;
        safeToInt(value);
    }

    @Test
    public void testSafeMultiplyToIntLong() {
        long val1 = 3L;
        long val2 = 4L;
        int result = safeMultiplyToInt(val1, val2);
        int expected = 12;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntLongOverflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 2;
        safeMultiplyToInt(val1, val2);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;
        verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsInvalid() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 32;
        int lowerBound = 1;
        int upperBound = 31;
        verifyValueBounds(field, value, lowerBound, upperBound);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        int expected = value;
        assertEquals(expected, result);
    }

    @Test
    public void testGetWrappedValueMinValue() {
        int value = Integer.MIN_VALUE;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        int expected = maxValue - (minValue - value) % (maxValue - minValue + 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWrappedValueMaxValue() {
        int value = Integer.MAX_VALUE;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        int expected = minValue;
        assertEquals(expected, result);
    }

    @Test
    public void testGetWrappedValueUnderflow() {
        int value = -1;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        int expected = maxValue - (minValue - value + 1) % (maxValue - minValue + 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetWrappedValueOverflow() {
        int value = 7;
        int minValue = 1;
        int maxValue = 5;
        int result = getWrappedValue(value, minValue, maxValue);
        int expected = value % (maxValue - minValue + 1) + minValue;
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsNull() {
        Object obj1 = null;
        Object obj2 = new Object();
        boolean result = equals(obj1, obj2);
        boolean expected = false;
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsSameObject() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        boolean result = equals(obj1, obj2);
        boolean expected = true;
        assertEquals(expected, result);
    }

    @Test
    public void testEqualsDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = equals(obj1, obj2);
        boolean expected = false;
        assertEquals(expected, result);
    }

    
}