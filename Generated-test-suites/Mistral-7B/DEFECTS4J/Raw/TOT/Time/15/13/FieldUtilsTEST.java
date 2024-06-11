package org.joda.time.field.util; // make sure the test package matches the production package

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the FieldUtils utility class.
 */
public class FieldUtilsTest {

    /**
     * Test safeNegate method with positive integer.
     */
    @Test
    public void testSafeNegatePositive() {
        int value = 10;
        int negated = FieldUtils.safeNegate(value);
        assertEquals("Negation of a positive integer", -value, negated);
    }

    /**
     * Test safeNegate method with negative integer.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeNegateNegative() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value); // should throw ArithmeticException
    }

    /**
     * Test safeAdd method with positive integers.
     */
    @Test
    public void testSafeAddPositive() {
        int val1 = 5;
        int val2 = 7;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals("Sum of two positive integers", val1 + val2, sum);
    }

    /**
     * Test safeAdd method with negative integers.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeAddNegative() {
        int val1 = -5;
        int val2 = -7;
        FieldUtils.safeAdd(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeAdd method with overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeAddOverflow() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        FieldUtils.safeAdd(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeSubtract method with positive integers.
     */
    @Test
    public void testSafeSubtractPositive() {
        int val1 = 10;
        int val2 = 3;
        long difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals("Difference of two positive integers", val1 - val2, difference);
    }

    /**
     * Test safeSubtract method with negative integers.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractNegative() {
        int val1 = -5;
        int val2 = -7;
        FieldUtils.safeSubtract(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeSubtract method with overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeSubtractOverflow() {
        int val1 = Integer.MIN_VALUE;
        int val2 = 1;
        FieldUtils.safeSubtract(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeMultiply method with positive integers.
     */
    @Test
    public void testSafeMultiplyPositive() {
        int val1 = 5;
        int val2 = 7;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product of two positive integers", val1 * val2, product);
    }

    /**
     * Test safeMultiply method with negative integers.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyNegative() {
        int val1 = -5;
        int val2 = 7;
        FieldUtils.safeMultiply(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeMultiply method with overflow for int.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyIntOverflow() {
        int val1 = Integer.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiply(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeMultiply method with long and int.
     */
    @Test
    public void testSafeMultiplyLongInt() {
        long val1 = 10L;
        int val2 = 5;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product of a long and an int", val1 * val2, product);
    }

    /**
     * Test safeMultiply method with overflow for long and int.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongIntOverflow() {
        long val1 = Long.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiply(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeMultiply method with two longs.
     */
    @Test
    public void testSafeMultiplyLongLong() {
        long val1 = 10L;
        long val2 = 5L;
        long product = FieldUtils.safeMultiply(val1, val2);
        assertEquals("Product of two longs", val1 * val2, product);
    }

    /**
     * Test safeMultiply method with overflow for two longs.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyLongLongOverflow() {
        long val1 = Long.MAX_VALUE;
        long val2 = 2L;
        FieldUtils.safeMultiply(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test safeToInt method with valid integer.
     */
    @Test
    public void testSafeToIntValidInt() {
        long value = 12345;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals("Cast of a long to an int", (int) value, intValue);
    }

    /**
     * Test safeToInt method with overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeToIntOverflow() {
        long value = Long.MAX_VALUE + 1;
        FieldUtils.safeToInt(value); // should throw ArithmeticException
    }

    /**
     * Test safeMultiplyToInt method with valid long and int.
     */
    @Test
    public void testSafeMultiplyToIntValid() {
        long val1 = 10L;
        int val2 = 5;
        int product = FieldUtils.safeMultiplyToInt(val1, val2);
        assertEquals("Multiplication of a long and an int, cast to int", val1 * val2, product);
    }

    /**
     * Test safeMultiplyToInt method with overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void testSafeMultiplyToIntOverflow() {
        long val1 = Integer.MAX_VALUE;
        int val2 = 2;
        FieldUtils.safeMultiplyToInt(val1, val2); // should throw ArithmeticException
    }

    /**
     * Test verifyValueBounds method with DateTimeField.
     */
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldOutOfBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 29, 2000, org.joda.time.LocalDate.FEBRUARY_28);
        int value = 30;
        FieldUtils.verifyValueBounds(field, value, 1, 31); // should throw IllegalFieldValueException
    }

    /**
     * Test verifyValueBounds method with DateTimeFieldType.
     */
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsDateTimeFieldTypeOutOfBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = 32;
        FieldUtils.verifyValueBounds(fieldType, value, 1, 31); // should throw IllegalFieldValueException
    }

    /**
     * Test verifyValueBounds method with String field name and int value.
     */
    @Test(expected = IllegalFieldValueException.class)
    public void testVerifyValueBoundsStringFieldOutOfBounds() {
        String fieldName = "dayOfMonth";
        int value = 32;
        FieldUtils.verifyValueBounds(fieldName, value, 1, 31); // should throw IllegalFieldValueException
    }

    /**
     * Test getWrappedValue method with valid value and range.
     */
    @Test
    public void testGetWrappedValueValid() {
        int value = 5;
        int minValue = 1;
        int maxValue = 10;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals("GetWrappedValue with valid value", value, wrappedValue);
    }

    /**
     * Test getWrappedValue method with value outside range.
     */
    @Test
    public void testGetWrappedValueOutsideRange() {
        int value = 12;
        int minValue = 1;
        int maxValue = 10;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals("GetWrappedValue with value outside range", (minValue + (maxValue - minValue)), wrappedValue);
    }

    /**
     * Test getWrappedValue method with negative value and range.
     */
    @Test
    public void testGetWrappedValueNegative() {
        int value = -5;
        int minValue = 1;
        int maxValue = 10;
        int wrappedValue = FieldUtils.getWrappedValue(value, minValue, maxValue);
        assertEquals("GetWrappedValue with negative value", (minValue + (maxValue - minValue - value)), wrappedValue);
    }

    /**
     * Test getWrappedValue method with minValue equal to maxValue.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetWrappedValueMinValueEqualsMaxValue() {
        int value = 1;
        int minValue = 1;
        int maxValue = 1;
        FieldUtils.getWrappedValue(value, minValue, maxValue); // should throw IllegalArgumentException
    }
}