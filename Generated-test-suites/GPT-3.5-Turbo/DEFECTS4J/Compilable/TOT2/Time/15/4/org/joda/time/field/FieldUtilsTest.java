package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals("Negate positive value", -10, FieldUtils.safeNegate(10));
        assertEquals("Negate negative value", 10, FieldUtils.safeNegate(-10));
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals("Add two positive values", 5, FieldUtils.safeAdd(2, 3));
        assertEquals("Add positive and negative value", 1, FieldUtils.safeAdd(3, -2));
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        assertEquals("Subtract two positive values", 3, FieldUtils.safeSubtract(5, 2));
        assertEquals("Subtract positive and negative value", 5, FieldUtils.safeSubtract(3, -2));
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Additional test cases for other methods can be added here

}