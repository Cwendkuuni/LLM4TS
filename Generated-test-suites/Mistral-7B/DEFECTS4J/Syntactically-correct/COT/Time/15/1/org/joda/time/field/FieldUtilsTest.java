package org.example; // replace with your test package name

import org.junit.Assert;
import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    

    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = -5;
        expected = 5;
        result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);

        value = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 2;
        int val2 = 3;
        long expected = 5;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 5;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 3;
        long expected = 7;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(expected, result);

        val1 = 5;
        val2 = 3;
        result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(2, result);

        val1 = 5;
        val2 = 5;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = 5;
        val2 = 11;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 2;
        int val2 = 3;
        int expected = 6;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(expected, result);

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 3;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 10;
        long val2 = 3;
        int expected = 30;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(expected, result);

        val1 = Long.MAX_VALUE;
        val2 = 5;
        Assert.assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1);

        int value = 7;
        int lowerBound = 1;
        int upperBound = 7;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 6;
        Assert.assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = 8;
        Assert.assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        field = new DateTimeField(DateTimeFieldType.centuryOfEra(), 1);
        value = 100;
        lowerBound = 0;
        upperBound = 100;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 99;
        Assert.assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        value = 101;
        Assert.assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        field = new DateTimeField(DateTimeFieldType.year(), 1);
        String fieldName = "year";
        value = 0;
        lowerBound = 0;
        upperBound = 3000;
        FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound);

        value = 3001;
        Assert.assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, lowerBound, upperBound));
    }

    @Test
    public void testGetWrappedValue() {
        int value = 5;
        int minValue = 0;
        int maxValue = 5;
        int expected = 5;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 1;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 6;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 7;
        expected = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 8;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = 5;
        maxValue = 5;
        value = 5;
        expected = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        value = 6;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = 0;
        maxValue = 4;
        value = 5;
        expected = 4;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = 4;
        maxValue = 0;
        value = 5;
        expected = 4;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = 5;
        maxValue = 5;
        value = 5;
        expected = 5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);

        minValue = 5;
        maxValue = 5;
        value = 6;
        expected = 1;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        Assert.assertFalse(FieldUtils.equals(object1, object2));

        Object object3 = new Object();
        Object object4 = object3;
        assertTrue(FieldUtils.equals(object3, object4));

        Object object5 = null;
        Assert.assertFalse(FieldUtils.equals(object1, object5));

        Object object6 = object1;
        assertTrue(FieldUtils.equals(object1, object6));
    }

    
}