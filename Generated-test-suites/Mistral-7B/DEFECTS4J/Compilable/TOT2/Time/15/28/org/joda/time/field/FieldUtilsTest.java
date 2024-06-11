package org.joda.time.field;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FieldUtilsTest {
    @Test
    public void testSafeNegate() {
        int value = 5;
        int expected = -5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinValue() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    @Test
    public void testSafeNegateNegative() {
        int value = -5;
        int expected = 5;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);
    }

    @Test
    public void testSafeNegateZero() {
        int value = 0;
        int expected = 0;
        int result = FieldUtils.safeNegate(value);
        assertEquals(expected, result);
    }
}