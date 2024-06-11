package org.joda.time.field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(Integer.MIN_VALUE));
    }

    @Test
    public void testSafeAdd() {
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));

        int a = Integer.MAX_VALUE;
        int b = 1;
        int c = FieldUtils.safeAdd(a, b);
        assertTrue(a + b > a);
        assertTrue(c == (a + b));

        long x = Long.MAX_VALUE;
        long y = 1;
        long z = FieldUtils.safeAdd(x, y);
        assertTrue(x + y > x);
        assertTrue(z == (x + y));
    }

    @Test
    public void testSafeSubtract() {
        long x = Long.MAX_VALUE;
        long y = 1;
        long z = FieldUtils.safeSubtract(x, y);
        assertTrue(x - y < x);
        assertTrue(z == (x - y));

        x = Long.MIN_VALUE;
        y = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(x, y));

        x = Integer.MIN_VALUE;
        y = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(x, y));
    }

    @Test
    public void testSafeMultiply() {
        int a = Integer.MIN_VALUE;
        int b = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(a, b));

        long x = Long.MAX_VALUE;
        long y = 2;
        long z = FieldUtils.safeMultiply(x, y);
        assertTrue(x * y > x);
        assertTrue(z == (x * y));

        x = Long.MIN_VALUE;
        y = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(x, y));

        x = Integer.MIN_VALUE;
        y = 2;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(x, y));
    }

    @Test
    public void testSafeMultiplyLongWithInt() {
        long x = 10;
        int y = 5;
        long z = FieldUtils.safeMultiply(x, y);
        assertTrue(x * y == z);
    }

    @Test
    public void testSafeMultiplyLongWithLong() {
        long x = 10;
        long y = 5;
        long z = FieldUtils.safeMultiply(x, y);
        assertTrue(x * y == z);
    }

    @Test
    public void testSafeMultiplyIntWithInt() {
        int x = 10;
        int y = 5;
        int z = FieldUtils.safeMultiply(x, y);
        assertTrue(x * y == z);
    }

    @Test
    public void testSafeMultiplyIntWithLong() {
        int x = 10;
        long y = 5;
        int z = FieldUtils.safeMultiplyToInt(x, y);
        assertTrue(x * y == z);
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = null;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(field, 1, 0, 1));

        DateTimeFieldType fieldType = DateTimeFieldType.millis();
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldType, 1000000000, 500, 1000));

        String fieldName = "test";
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, 1000000000, 500, 1000));

        DateTimeField dayOfMonth = DateTimeField.dayOfMonth();
        FieldUtils.verifyValueBounds(dayOfMonth, 31, 1, 31);
        FieldUtils.verifyValueBounds(dayOfMonth, 30, 1, 30);
        FieldUtils.verifyValueBounds(dayOfMonth, 32, 1, 31);
        FieldUtils.verifyValueBounds(dayOfMonth, 0, 1, 31);
        FieldUtils.verifyValueBounds(dayOfMonth, -1, 1, 31);

        DateTimeFieldType hourOfDay = DateTimeFieldType.hourOfDay();
        FieldUtils.verifyValueBounds(hourOfDay, 24, 0, 23);
        FieldUtils.verifyValueBounds(hourOfDay, 25, 0, 23);
        FieldUtils.verifyValueBounds(hourOfDay, -1, 0, 23);
    }

    @Test
    public void testGetWrappedValue() {
        int minValue = 0;
        int maxValue = 5;

        int value1 = 0;
        int wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertTrue(wrappedValue1 == minValue);

        value1 = minValue - 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertTrue(wrappedValue1 == maxValue);

        value1 = maxValue + 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertTrue(wrappedValue1 == minValue);

        value1 = Integer.MIN_VALUE;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertTrue(wrappedValue1 == maxValue);

        value1 = Integer.MAX_VALUE;
        wrappedValue1 = FieldUtils.getWrappedValue(value1, 1, minValue, maxValue);
        assertTrue(wrappedValue1 == minValue);

        int value2 = Integer.MIN_VALUE - 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value2, -1, minValue, maxValue);
        assertTrue(wrappedValue1 == maxValue);

        int value3 = Integer.MAX_VALUE + 1;
        wrappedValue1 = FieldUtils.getWrappedValue(value3, -1, minValue, maxValue);
        assertTrue(wrappedValue1 == minValue);

        int value4 = Integer.MIN_VALUE;
        wrappedValue1 = FieldUtils.getWrappedValue(value4, 2, minValue, maxValue);
        assertTrue(wrappedValue1 == (minValue + 2));

        int value5 = Integer.MAX_VALUE;
        wrappedValue1 = FieldUtils.getWrappedValue(value5, 2, minValue, maxValue);
        assertTrue(wrappedValue1 == (minValue - 2));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        Object obj3 = obj1;
        assertTrue(FieldUtils.equals(obj1, obj3));

        Object obj4 = null;
        assertFalse(FieldUtils.equals(obj1, obj4));
    }
}