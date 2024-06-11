package org.joda.time.field;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        value = -5;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        value = Integer.MIN_VALUE;
        try {
            result = FieldUtils.safeNegate(value);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        int val1 = 5, val2 = 3, sum;
        sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(8, sum);

        val1 = Integer.MIN_VALUE;
        val2 = 3;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        val1 = 5;
        val2 = Integer.MIN_VALUE;
        try {
            sum = FieldUtils.safeAdd(val1, val2);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Add remaining safeAdd, safeSubtract, safeMultiply test cases here

    // Add test cases for other methods in the class here
}