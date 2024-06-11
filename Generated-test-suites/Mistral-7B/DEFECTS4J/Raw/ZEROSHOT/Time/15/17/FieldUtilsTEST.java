package org.joda.time.test.field;

import org.junit.Test;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.field.FieldUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-1, FieldUtils.safeNegate(1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(-1));
    }

    @Test
    public void testSafeAdd() {
        assertEquals(2, FieldUtils.safeAdd(1, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(0, FieldUtils.safeSubtract(0, 0));
        assertEquals(1, FieldUtils.safeSubtract(2, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Integer.MIN_VALUE, 1));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(1, Integer.MIN_VALUE));
    }

    @Test
    public void testSafeMultiply() {
        assertEquals(0, FieldUtils.safeMultiply(0, 0));
        assertEquals(4, FieldUtils.safeMultiply(2, 2));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testSafeMultiplyLongToInt() {
        assertEquals(0, FieldUtils.safeMultiplyToInt(0, 0));
        assertEquals(6, FieldUtils.safeMultiplyToInt(2, 3));
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(Integer.MIN_VALUE, 2));
    }

    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");

        assertThrows(IllegalFieldValueException.class, () -> FieldUtils.verifyValueBounds(field, 32, 1, 31));
        assertEquals(31, FieldUtils.verifyValueBounds(field, 31, 1, 31));
        assertEquals(1, FieldUtils.verifyValueBounds(field, 1, 1, 31));
    }

    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsWithValuesOutOfBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        FieldUtils.verifyValueBounds(field, 33, 1, 31);
    }

    @Test
    public void testGetWrappedValue() {
        assertEquals(1, FieldUtils.getWrappedValue(1, 3, 0, 5));
        assertEquals(4, FieldUtils.getWrappedValue(6, 3, 0, 5));
        assertEquals(2, FieldUtils.getWrappedValue(-1, 3, 0, 5));
        assertEquals(5, FieldUtils.getWrappedValue(-5, 3, 0, 5));
    }

    @Test
    public void testGetWrappedValueWithMinValueGreaterThanMaxValue() {
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.getWrappedValue(1, 3, 5, 0));
    }
}