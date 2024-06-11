package org.joda.time.field.util; // import statements go here

import static org.junit.Assert.*;

import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for {@link FieldUtils}
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = -5;
        negatedValue = FieldUtils.safeNegate(value);
        assertEquals(5, negatedValue);

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeAdd method
     */
    @Test
    public void testSafeAdd() {
        int value1 = 5;
        int value2 = 3;
        int sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(8, sum);

        value1 = 5;
        value2 = -3;
        sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(2, sum);

        value1 = Integer.MAX_VALUE;
        value2 = 1;
        sum = FieldUtils.safeAdd(value1, value2);
        assertEquals(Integer.MAX_VALUE, sum);

        value1 = Integer.MIN_VALUE;
        value2 = 1;
        try {
            FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeSubtract method
     */
    @Test
    public void testSafeSubtract() {
        long value1 = 10L;
        long value2 = 3L;
        long difference = FieldUtils.safeSubtract(value1, value2);
        assertEquals(7L, difference);

        value1 = 10L;
        value2 = -3L;
        difference = FieldUtils.safeSubtract(value1, value2);
        assertEquals(13L, difference);

        value1 = -10L;
        value2 = 3L;
        difference = FieldUtils.safeSubtract(value1, value2);
        assertEquals(7L, difference);

        value1 = Long.MIN_VALUE;
        value2 = 1L;
        try {
            FieldUtils.safeSubtract(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method
     */
    @Test
    public void testSafeMultiply() {
        int value1 = 5;
        int value2 = 3;
        int product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15, product);

        value1 = Integer.MIN_VALUE;
        value2 = 2;
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = 5;
        value2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method with long
     */
    @Test
    public void testSafeMultiplyWithLong() {
        long value1 = 5L;
        int value2 = 3;
        long product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15L, product);

        value1 = 5L;
        long value3 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        int value4 = 3;
        try {
            FieldUtils.safeMultiply(value1, value4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Integer.MIN_VALUE;
        value2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiply method with long and long
     */
    @Test
    public void testSafeMultiplyWithLongLong() {
        long value1 = 5L;
        long value2 = 3L;
        long product = FieldUtils.safeMultiply(value1, value2);
        assertEquals(15L, product);

        value1 = 5L;
        long value3 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value3);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        long value4 = 3L;
        try {
            FieldUtils.safeMultiply(value1, value4);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeToInt method
     */
    @Test
    public void testSafeToInt() {
        long value = 123L;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(123, intValue);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value = Long.MAX_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for safeMultiplyToInt method
     */
    @Test
    public void testSafeMultiplyToInt() {
        long value1 = 5L;
        long value2 = 3L;
        int product = FieldUtils.safeMultiplyToInt(value1, value2);
        assertEquals(15, product);

        value1 = 5L;
        value2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = 3L;
        try {
            FieldUtils.safeMultiplyToInt(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        value1 = Long.MIN_VALUE;
        value2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeMultiplyToInt(value1, value2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    /**
     * Test for verifyValueBounds method
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        try {
            FieldUtils.verifyValueBounds(fieldType, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        DateTimeField field = new DateTimeField(fieldType, 1, 2000);
        try {
            FieldUtils.verifyValueBounds(field, 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds("dayOfMonth", 32, 1, 31);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 29, 1, 31);
            fail("Expected IllegalFieldValueException for February");
        } catch (IllegalFieldValueException e) {
            // expected
        }

        try {
            FieldUtils.verifyValueBounds(fieldType, 30, 1, 31, true); // leap year
            assertEquals(29, FieldUtils.getWrappedValue(29, 28, 1, 31)); // non-leap year
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
}