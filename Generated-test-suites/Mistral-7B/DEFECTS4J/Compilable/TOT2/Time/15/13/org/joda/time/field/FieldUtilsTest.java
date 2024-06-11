package org.joda.time.field;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int input = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(input);
        assertEquals(expected, result);

        // Edge case: minimum integer value
        input = Integer.MIN_VALUE;
        expected = -Integer.MIN_VALUE;
        result = FieldUtils.safeNegate(input);
        assertEquals(expected, result);

        // Error case: input as minimum integer value
        try {
            FieldUtils.safeNegate(Integer.MIN_VALUE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Repeat this pattern for other methods: safeAdd, safeSubtract, safeMultiply, etc.

    // Test all methods here

    
}