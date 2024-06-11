package org.joda.time.field;
import org.junit.Test;
import org.joda.time.field.FieldUtils;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int input = 5;
        int expectedOutput = -5;
        int output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Edge case: minimum integer value
        input = Integer.MIN_VALUE;
        expectedOutput = -Integer.MIN_VALUE;
        output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Exception test: minimum integer value
        input = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(input);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    // ... similar test cases for other methods in FieldUtils class

    // 
}