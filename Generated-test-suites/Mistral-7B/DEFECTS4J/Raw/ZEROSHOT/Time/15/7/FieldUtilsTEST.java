package org.joda.time.test.field;

import org.junit.Assert;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
        } catch (ArithmeticException e) {
            Assert.assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        long sum = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(-Integer.MIN_VALUE + 1, sum);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + val1 + " + " + val2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeAddLong() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1;
        long sum = FieldUtils.safeAdd(val1, val2);
        Assert.assertEquals(-Long.MIN_VALUE + 1, sum);

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + val1 + " + " + val2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = Long.MAX_VALUE;
        long val2 = 1;
        long diff = FieldUtils.safeSubtract(val1, val2);
        Assert.assertEquals(Long.MAX_VALUE - 1, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("The calculation caused an overflow: " + val1 + " - " + val2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = Integer.MIN_VALUE;
        int val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows an int: " + val1 + " * " + val2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = Long.MIN_VALUE;
        int val2 = Integer.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: " + val1 + " * " + val2, e.getMessage());
        }
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = Long.MIN_VALUE;
        long val2 = Long.MIN_VALUE;
        long total = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(Long.MIN_VALUE * Long.MIN_VALUE, total);

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: " + val1 + " * " + val2, e.getMessage());
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
        } catch (ArithmeticException e) {
            Assert.assertEquals("Multiplication overflows a long: " + val1 + " * " + val2, e.getMessage());
        }

        val1 = Long.MAX_VALUE;
        val2 = Long.MAX_VALUE;
        long total2 = FieldUtils.safeMultiply(val1, val2);
        Assert.assertEquals(Long.MAX_VALUE * Long.MAX_VALUE, total2);
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
        } catch (ArithmeticException e) {
            Assert.fail("Value cannot fit in an int: " + value);
        }
        Assert.assertEquals(Integer.MAX_VALUE, FieldUtils.safeToInt(value));

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
        } catch (ArithmeticException e) {
            Assert.fail("Value cannot fit in an int: " + value);
        }
        Assert.assertEquals(Integer.MIN_VALUE, FieldUtils.safeToInt(value));
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = Long.MIN_VALUE;
        long val2 = Long.MAX_VALUE;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        Assert.assertEquals(Integer.MIN_VALUE, result);
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "test");
        try {
            FieldUtils.verifyValueBounds(field, 7, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.fail("Expected no exception, but got: " + e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(field, 8, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 8 is out of range (valid values are 0 to 6)", e.getMessage());
        }
    }

    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        try {
            FieldUtils.verifyValueBounds(fieldType, 7, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.fail("Expected no exception, but got: " + e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 8, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 8 is out of range (valid values are 0 to 6)", e.getMessage());
        }
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "test";
        try {
            FieldUtils.verifyValueBounds(fieldName, 7, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.fail("Expected no exception, but got: " + e.getMessage());
        }

        try {
            FieldUtils.verifyValueBounds(fieldName, 8, 0, 6);
        } catch (IllegalFieldValueException e) {
            Assert.assertEquals("Value 8 is out of range (valid values are 0 to 6)", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int currentValue = Integer.MIN_VALUE;
        int wrapValue = 1;
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MIN_VALUE + 2;
        int wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrapValue = -1;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE + 2;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(maxValue, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrapValue = 0;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE + 2;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(minValue, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrapValue = 3;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE + 2;
        wrappedValue = FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        Assert.assertEquals(minValue + 2, wrappedValue);

        currentValue = Integer.MIN_VALUE;
        wrapValue = 3;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MIN_VALUE;
        try {
            FieldUtils.getWrappedValue(currentValue, wrapValue, minValue, maxValue);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("MIN > MAX", e.getMessage());
        }
    }

    @org.junit.Test
    public void testEquals() {
        Object object1 = new Object();
        Object object2 = new Object();
        Assert.assertFalse(FieldUtils.equals(object1, object2));

        Object object3 = object1;
        Assert.assertTrue(FieldUtils.equals(object1, object3));

        object3 = null;
        Assert.assertFalse(FieldUtils.equals(object1, object3));
    }
}