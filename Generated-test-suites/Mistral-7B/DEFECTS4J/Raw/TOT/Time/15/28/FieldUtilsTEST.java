package org.joda.time.field.util; // Make sure to import all necessary classes from the package

import static org.junit.Assert.*;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for the FieldUtils class
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method
     */
    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        result = FieldUtils.safeNegate(value);
        assertEquals(5, result);

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeAdd method
     */
    @Test
    public void testSafeAdd() {
        int val1 = 1, val2 = 2, sum;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(3, sum);

        val1 = 5;
        val2 = 3;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 1;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeSubtract method
     */
    @Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 2L, difference;
        difference = FieldUtils.safeSubtract(val1, val2);
        assertEquals(8, difference);

        val1 = 1L;
        val2 = 10L;
        try {
            difference = FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeMultiply method
     */
    @Test
    public void testSafeMultiply() {
        int val1 = 2, val2 = 3, product;
        product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 2;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            product = FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for verifyValueBounds method
     */
    @Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), "test");
        int value = 7;

        try {
            FieldUtils.verifyValueBounds(field, value, 1, 6);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        FieldUtils.verifyValueBounds(field, value, 1, 7);
        assertEquals(value, FieldUtils.getWrappedValue(value, 1, 7));

        value = 0;
        FieldUtils.verifyValueBounds(field, value, 1, 7);
        assertEquals(value, FieldUtils.getWrappedValue(value, 1, 7));

        value = 8;
        try {
            FieldUtils.verifyValueBounds(field, value, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test for DateTimeFieldType
        FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfWeek(), value, 1, 7);
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.dayOfWeek(), value, 1, 6);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        // Test for String
        String fieldName = "test";
        try {
            FieldUtils.verifyValueBounds(fieldName, value, 1, 6);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }
    }
}