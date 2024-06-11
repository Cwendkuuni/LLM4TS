package org.joda.time.field;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(-5, result);

        // Edge cases
        int minValue = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minValue);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        int maxValue = Integer.MAX_VALUE;
        int negatedMaxValue = FieldUtils.safeNegate(maxValue);
        assertEquals(-(maxValue + 1), negatedMaxValue);
    }

    @Test
    public void testSafeAdd() {
        // Basic test
        int val1 = 2;
        int val2 = 3;
        int sum = FieldUtils.safeAdd(val1, val2);
        assertEquals(val1 + val2, sum);

        // Edge cases
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        int sum1 = FieldUtils.safeAdd(minValue, 1);
        int sum2 = FieldUtils.safeAdd(maxValue, 1);
        assertEquals(minValue + 1, sum1);
        assertEquals(maxValue + 1, sum2);

        int sum3 = FieldUtils.safeAdd(minValue, maxValue);
        try {
            sum3 = FieldUtils.safeAdd(sum3, 1);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    //... other test cases for safeSubtract, safeMultiply, safeMultiplyToInt
}