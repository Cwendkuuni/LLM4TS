package org.joda.time.test.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

public class FieldUtilsTest {

    @org.junit.Test
    public void testSafeNegate() {
        int value = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(value));

        value = 5;
        assertEquals(-5, FieldUtils.safeNegate(value));
    }

    @org.junit.Test
    public void testSafeAdd() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));

        val1 = 5;
        val2 = 3;
        assertEquals(8, FieldUtils.safeAdd(val1, val2));

        val1 = Integer.MAX_VALUE;
        val2 = 1;
        assertEquals(Integer.MAX_VALUE + 1, FieldUtils.safeAdd(val1, val2));

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(val1, val2));
    }

    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = Long.MIN_VALUE;
        long val2 = 1;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));

        val1 = 5;
        val2 = 3;
        assertEquals(2, FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MAX_VALUE;
        val2 = 1;
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(val1, val2));

        val1 = Long.MAX_VALUE;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = Integer.MIN_VALUE;
        int val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5;
        val2 = 3;
        assertEquals(15, FieldUtils.safeMultiply(val1, val2));

        val1 = Integer.MAX_VALUE;
        val2 = Integer.MAX_VALUE;
        assertEquals(Long.MAX_VALUE, (long) FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiplyLongInt() {
        long val1 = Long.MIN_VALUE;
        int val2 = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5;
        val2 = 3;
        assertEquals(15, FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MAX_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeMultiplyLongLong() {
        long val1 = Long.MIN_VALUE;
        long val2 = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(val1, val2));

        val1 = 5L;
        val2 = 3L;
        assertEquals(15L, FieldUtils.safeMultiply(val1, val2));

        val1 = Long.MAX_VALUE;
        val2 = Long.MAX_VALUE;
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(val1, val2));
    }

    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeToInt((int) value));

        value = 123;
        assertEquals(123, FieldUtils.safeToInt((int) value));
    }

    @org.junit.Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        assertEquals(15, FieldUtils.safeMultiplyToInt(val1, val2));

        val1 = Long.MAX_VALUE;
        val2 = 2;
        assertEquals(Long.MAX_VALUE * 2, FieldUtils.safeMultiplyToInt(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 2;
        assertEquals(Long.MIN_VALUE * 2, FieldUtils.safeMultiplyToInt(val1, val2));
    }

    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "dayOfWeek", 1, 7);

        int value = 8;
        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, value, 1, 7));

        value = 1;
        FieldUtils.verifyValueBounds(field, value, 1, 7);

        value = 7;
        FieldUtils.verifyValueBounds(field, value, 1, 7);
    }

    @org.junit.Test
    public void testVerifyValueBoundsDateTimeFieldType() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();

        int value = 8;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, 1, 7));

        value = 1;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 7);

        value = 7;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 7);
    }

    @org.junit.Test
    public void testVerifyValueBoundsString() {
        String fieldName = "dayOfWeek";

        int value = 8;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName, value, 1, 7));

        value = 1;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 7);

        value = 7;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 7);
    }

    @org.junit.Test
    public void testGetWrappedValue() {
        int value = Integer.MIN_VALUE;
        int minValue = 1;
        int maxValue = 5;
        assertEquals(minValue, FieldUtils.getWrappedValue(value, minValue, maxValue));

        value = Integer.MIN_VALUE - 1;
        assertEquals(maxValue, FieldUtils.getWrappedValue(value, minValue, maxValue));

        value = Integer.MAX_VALUE;
        assertEquals(minValue, FieldUtils.getWrappedValue(value, minValue, maxValue));

        value = Integer.MAX_VALUE + 1;
        assertEquals(minValue, FieldUtils.getWrappedValue(value, minValue, maxValue));
    }

    @org.junit.Test
    public void testGetWrappedValueMinGreaterThanMax() {
        int minValue = 5;
        int maxValue = 1;
        int value = 3;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(value, minValue, maxValue));
    }
}