package org.joda.time.field.util; // Assuming the package name is correct

import static org.junit.Assert.*;

import org.joda.time.field.DateTimeField;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Test for safeNegate method.
     */
    @org.junit.Test
    public void testSafeNegate() {
        int value = 5;
        int negatedValue = FieldUtils.safeNegate(value);
        assertEquals(-5, negatedValue);

        value = -5;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeAdd method.
     */
    @org.junit.Test
    public void testSafeAdd() {
        int val1 = 3, val2 = 2;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(5, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 1;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1, sum); // Integer.MIN_VALUE + 1 == Integer.MIN_VALUE

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeSubtract method.
     */
    @org.junit.Test
    public void testSafeSubtract() {
        long val1 = 10L, val2 = 5L;
        long diff = FieldUtils.safeSubtract(val1, val2);
        assertEquals(5L, diff);

        val1 = Long.MIN_VALUE;
        val2 = 1;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Long.MIN_VALUE;
        val2 = Long.MIN_VALUE;
        try {
            FieldUtils.safeSubtract(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeMultiply method.
     */
    @org.junit.Test
    public void testSafeMultiply() {
        int val1 = 2, val2 = 3;
        int product = FieldUtils.safeMultiply(val1, val2);
        assertEquals(6, product);

        val1 = Integer.MIN_VALUE;
        val2 = 2;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = Integer.MIN_VALUE;
        val2 = Integer.MIN_VALUE;
        try {
            FieldUtils.safeMultiply(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for safeToInt method.
     */
    @org.junit.Test
    public void testSafeToInt() {
        long value = Long.MAX_VALUE;
        int intValue = FieldUtils.safeToInt(value);
        assertEquals(Integer.MAX_VALUE, intValue);

        value = Long.MIN_VALUE;
        try {
            FieldUtils.safeToInt(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test for verifyValueBounds method.
     */
    @org.junit.Test
    public void testVerifyValueBounds() {
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfWeek(), 1);

        try {
            FieldUtils.verifyValueBounds(field, 8, 1, 7);
            fail("Expected IllegalFieldValueException");
        } catch (IllegalFieldValueException e) {
            // Expected
        }

        try {
            FieldUtils.verifyValueBounds(field, 1, 1, 7);
            // Expected success
        } catch (IllegalFieldValueException e) {
            fail("Unexpected IllegalFieldValueException");
        }
    }

    /**
     * Test for equals method.
     */
    @org.junit.Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object("same");
        Object obj3 = new Object();

        assertTrue(FieldUtils.equals(obj1, obj1));
        assertTrue(FieldUtils.equals(obj1, obj2)); // Since they have the same value
        assertFalse(FieldUtils.equals(obj1, obj3));
    }
}