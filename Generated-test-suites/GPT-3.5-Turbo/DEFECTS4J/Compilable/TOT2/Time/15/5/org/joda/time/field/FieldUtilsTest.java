package org.joda.time.field;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        assertEquals(-5, FieldUtils.safeNegate(5));
        assertEquals(0, FieldUtils.safeNegate(0));
        // Test for exception when value is Integer.MIN_VALUE
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        assertEquals(10, FieldUtils.safeAdd(5, 5));
        assertEquals(100, FieldUtils.safeAdd(50, 50));
        // Test for exception when overflow occurs
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        assertEquals(-10, FieldUtils.safeSubtract(-5, 5));
        // Test for exception when overflow occurs
        try {
            FieldUtils.safeSubtract(Long.MIN_VALUE, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Add more test cases for other methods in FieldUtils class

}