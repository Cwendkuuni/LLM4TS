package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.0;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 2.0;
        double offset = 3.0;
        final double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_Exception() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    // Continue with other methods...

}