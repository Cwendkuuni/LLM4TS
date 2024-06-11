package org.joda.time.field;
import static org.junit.Assert.*;
import org.junit.Test;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Typical use case
        assertEquals(-10, FieldUtils.safeNegate(10));
        
        // Edge case - Integer.MIN_VALUE
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Should throw ArithmeticException for Integer.MIN_VALUE");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeAdd() {
        // Typical use case
        assertEquals(15, FieldUtils.safeAdd(10, 5));
        
        // Edge case - overflow
        try {
            FieldUtils.safeAdd(Integer.MAX_VALUE, 1);
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        // Typical use case
        assertEquals(5, FieldUtils.safeSubtract(10, 5));
        
        // Edge case - overflow
        try {
            FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE);
            fail("Should throw ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Add test cases for safeMultiply, safeToInt, verifyValueBounds, getWrappedValue, and equals() methods with typical use cases, edge cases, and error scenarios

}