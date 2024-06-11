package org.junit.joda.time.field.FieldUtilsTest;

import static org.junit.joda.time.field.FieldUtils.*;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for FieldUtils.
 */
public class FieldUtilsTest {

    /**
     * Test safeNegate method.
     */
    @Test
    public void testSafeNegate() {
        int input = 10;
        int expected = -input;
        int actual = safeNegate(input);
        assertEquals(expected, actual);

        input = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> safeNegate(input));
    }

    /**
     * Test safeAdd method.
     */
    @Test
    public void testSafeAdd() {
        int val1 = 5;
        int val2 = 3;
        long expected = val1 + val2;
        long actual = safeAdd(val1, val2);
        assertEquals(expected, actual);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        expected = Integer.MIN_VALUE + 1;
        actual = safeAdd(val1, val2);
        assertEquals(expected, actual);

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> safeAdd(val1, val2));
    }

    /**
     * Test safeSubtract method.
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L;
        long val2 = 3L;
        long expected = val1 - val2;
        long actual = safeSubtract(val1, val2);
        assertEquals(expected, actual);

        val1 = 10L;
        val2 = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> safeSubtract(val1, val2));
    }

    /**
     * Test safeMultiply method.
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 5;
        int val2 = 3;
        long expected = (long) val1 * (long) val2;
        long actual = safeMultiply(val1, val2);
        assertEquals(expected, actual);

        val1 = Integer.MIN_VALUE;
        val2 = 4;
        assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));
    }

    /**
     * Test safeMultiply method with long and int.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 5L;
        int val2 = 3;
        long expected = val1 * val2;
        long actual = safeMultiply(val1, val2);
        assertEquals(expected, actual);

        val1 = 5L;
        val2 = -1;
        long actualNegative = safeMultiply(val1, val2);
        long expectedNegative = -val1;
        assertEquals(expectedNegative, actualNegative);

        val1 = 0L;
        val2 = 0;
        long expectedZero = 0L;
        long actualZero = safeMultiply(val1, val2);
        assertEquals(expectedZero, actualZero);

        val1 = Long.MIN_VALUE;
        val2 = 4;
        assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));
    }

    /**
     * Test safeMultiply method with long and long.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 5L;
        long val2 = 3L;
        long expected = val1 * val2;
        long actual = safeMultiply(val1, val2);
        assertEquals(expected, actual);

        val1 = 5L;
        val2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = 5L;
        assertThrows(ArithmeticException.class, () -> safeMultiply(val1, val2));

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        long expectedMinMin = Long.MIN_VALUE * Long.MIN_VALUE;
        long actualMinMin = safeMultiply(val1, val2);
        assertEquals(expectedMinMin, actualMinMin);
    }

    /**
     * Test safeToInt method.
     */
    @Test
    public void testSafeToInt() {
        long input = Integer.MAX_VALUE;
        int expected = (int) input;
        int actual = safeToInt(input);
        assertEquals(expected, actual);

        input = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> safeToInt(input));
    }

    /**
     * Test safeMultiplyToInt method.
     */
    @Test
    public void testSafeMultiplyToInt() {
        long val1 = 5L;
        long val2 = 3L;
        int expected = (int) (val1 * val2);
        int actual = safeMultiplyToInt(val1, val2);
        assertEquals(expected, actual);

        val1 = Long.MIN_VALUE;
        val2 = 4L;
        assertThrows(ArithmeticException.class, () -> safeMultiplyToInt(val1, val2));
    }

    /**
     * Test verifyValueBounds method with DateTimeField and int.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldInt() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), "dayOfMonth");
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;

        verifyValueBounds(field, value, lowerBound, upperBound);
    }

    /**
     * Test verifyValueBounds method with DateTimeFieldType and int.
     */
    @Test
    public void testVerifyValueBoundsDateTimeFieldTypeInt() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;

        verifyValueBounds(fieldType, value, lowerBound, upperBound);
    }

    /**
     * Test verifyValueBounds method with String and int.
     */
    @Test
    public void testVerifyValueBoundsStringInt() {
        String fieldName = "dayOfMonth";
        int value = 31;
        int lowerBound = 1;
        int upperBound = 30;

        verifyValueBounds(fieldName, value, lowerBound, upperBound);
    }

    /**
     * Test getWrappedValue method with int.
     */
    @Test
    public void testGetWrappedValueInt() {
        int value = 5;
        int minValue = 0;
        int maxValue = 7;
        int expected = 5;

        int actual = getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, actual);
    }

    /**
     * Test getWrappedValue method with int negative value.
     */
    @Test
    public void testGetWrappedValueIntNegative() {
        int value = -5;
        int minValue = 0;
        int maxValue = 7;
        int expected = 2;

        int actual = getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, actual);
    }

    /**
     * Test getWrappedValue method with int value greater than max value.
     */
    @Test
    public void testGetWrappedValueIntGreaterThanMax() {
        int value = 8;
        int minValue = 0;
        int maxValue = 7;
        int expected = 1;

        int actual = getWrappedValue(value, minValue, maxValue);
        assertEquals(expected, actual);
    }

    /**
     * Test getWrappedValue method with int minValue equal to maxValue.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueIntMinGreaterThanMax() {
        int value = 0;
        int minValue = 8;
        int maxValue = 7;
        getWrappedValue(value, minValue, maxValue);
    }
}