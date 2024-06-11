package org.joda.time.test.field;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 10;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-10, result);

        value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));
    }

    @Test
    public void testSafeAdd() {
        int val1 = 10;
        int val2 = 5;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(15, result);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeAddLong() {
        long val1 = 10;
        long val2 = 5;
        long result = FieldUtils.safeAdd(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        result = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @Test
    public void testSafeSubtract() {
        long val1 = 10;
        long val2 = 5;
        long result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        result = FieldUtils.safeSubtract(val1, val2);
        assertEquals(val1, result);
    }

    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        int result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5;
        int val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5;
        long val2 = 3;
        long result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        result = FieldUtils.safeMultiply(val1, val2);
        assertEquals(val1 * val2, result);

        val1 = Long.MIN_VALUE;
        val2 = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @Test
    public void testSafeToInt() {
        long value = 10;
        int result = FieldUtils.safeToInt(value);
        assertEquals(10, result);

        value = Integer.MIN_VALUE + 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt(value));
    }

    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5;
        long val2 = 3;
        int result = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals(15, result);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "test", null);

        int value = 32;
        int lowerBound = 1;
        int upperBound = 31;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 1;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 0;
        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        value = 33;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound));

        field = new DateTimeField(DateTimeFieldType.hourOfDay(), "test", null);

        value = 24;
        FieldUtils.verifyValueBounds(field, value, 0, 23);

        value = 0;
        FieldUtils.verifyValueBounds(field, value, 0, 23);

        value = 25;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 0, 23));

        field = null;

        value = 1;
        String fieldName = "test";
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 0, 23));

        value = 31;
        FieldUtils.verifyValueBounds(fieldName, value, 0, 30);
    }

    @Test
    public void testGetWrappedValue() {
        int value = 15;
        int minValue = 1;
        int maxValue = 10;
        int result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(value % (maxValue - minValue + 1) + minValue, result);

        value = 0;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(minValue, result);

        value = -5;
        result = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals(maxValue - (Math.abs(value) % (maxValue - minValue + 1)), result);

        minValue = maxValue;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(FieldUtils.equals(obj1, obj2));

        obj1 = obj2;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        assertTrue(FieldUtils.equals(obj1, obj2));

        obj1 = new Object();
        assertTrue(FieldUtils.equals(obj1, obj1));
    }
}