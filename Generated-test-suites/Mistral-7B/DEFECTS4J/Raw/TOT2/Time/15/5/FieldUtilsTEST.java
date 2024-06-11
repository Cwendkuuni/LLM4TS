// FieldUtilsTest.java

package org.junit.tests;

import org.joda.time.field.FieldUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        // Basic test
        int input = 10;
        int expected = -10;
        int output = FieldUtils.safeNegate(input);
        assertEquals(expected, output);

        // Edge cases
        int minInt = Integer.MIN_VALUE;
        try {
            FieldUtils.safeNegate(minInt);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        int maxInt = Integer.MAX_VALUE;
        int output2 = FieldUtils.safeNegate(maxInt);
        assertEquals(-maxInt, output2);
    }

    @Test
    public void testSafeAdd() {
        // Basic test
        int a = 10;
        int b = 5;
        int expected = 15;
        int output = FieldUtils.safeAdd(a, b);
        assertEquals(expected, output);

        // Edge cases
        long minLong = Long.MIN_VALUE;
        long maxLong = Long.MAX_VALUE;
        long aLong = -(minLong + 1);
        long bLong = 1;
        long expectedLong = aLong + bLong;
        long outputLong = FieldUtils.safeAdd((int) aLong, (int) b);
        assertEquals(expectedLong, outputLong);

        long minIntToLongCast = Integer.MIN_VALUE;
        long maxIntToLongCast = Integer.MAX_VALUE;
        long aIntToLongCast = (long) minIntToLongCast;
        long bIntToLongCast = (long) (minIntToLongCast + 1);
        long expectedIntToLongCast = aIntToLongCast + bIntToLongCast;
        long outputIntToLongCast = FieldUtils.safeAdd((int) aIntToLongCast, (int) bIntToLongCast);
        assertEquals(expectedIntToLongCast, outputIntToLongCast);

        // Exception test
        int input = Integer.MIN_VALUE;
        int addend = 1;
        try {
            FieldUtils.safeAdd(input, addend);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSafeSubtract() {
        // Basic test
        long a = 10L;
        long b = 5L;
        long expected = 5L;
        long output = FieldUtils.safeSubtract(a, b);
        assertEquals(expected, output);

        // Edge cases
        long minLong = Long.MIN_VALUE;
        long maxLong = Long.MAX_VALUE;
        long aLong = maxLong;
        long bLong = 1;
        long expectedLong = aLong - bLong;
        long outputLong = FieldUtils.safeSubtract(aLong, b);
        assertEquals(expectedLong, outputLong);

        long minIntToLongCast = Integer.MIN_VALUE;
        long maxIntToLongCast = Integer.MAX_VALUE;
        long aIntToLongCast = (long) minIntToLongCast;
        long bIntToLongCast = (long) (minIntToLongCast + 1);
        long expectedIntToLongCast = aIntToLongCast - bIntToLongCast;
        long outputIntToLongCast = FieldUtils.safeSubtract(aIntToLongCast, bIntToLongCast);
        assertEquals(expectedIntToLongCast, outputIntToLongCast);

        // Exception test
        long input = Long.MIN_VALUE;
        long subtrahend = 1;
        try {
            FieldUtils.safeSubtract(input, subtrahend);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    // Other test methods for other methods in the class
}