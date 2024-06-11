package org.apache.commons.math3.util;

import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    private static final double EPSILON = 1e-10;

    @Test
    public void testHash_double() {
        double value = 3.141592653589793;
        int expected = -1719713608;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] values = {1.0, 2.0, 3.0, 4.0, 5.0};
        int expected = 1062989255;
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 8.0;
        double center = 2.0;
        double expected = 5.0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testReduce() {
        double a = 15.0;
        double period = 6.0;
        double offset = 4.0;
        double expected = 5.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = -127;
        byte sign = 1;
        byte expected = -127;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException ex) {
            // Success
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = -32767;
        short sign = 1;
        short expected = -32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException ex) {
            // Success
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = -2147483647;
        int sign = 1;
        int expected = -2147483647;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException ex) {
            // Success
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = -9223372036854775806L;
        long sign = 1;
        long expected = -9223372036854775806L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException ex) {
            // Success
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 3.14;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException ex) {
            fail("NotFiniteNumberException not expected");
        }
    }

    @Test
    public void testCheckFinite_double_infinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException ex) {
            // Success
        }
    }

    @Test
    public void testCheckFinite_double_nan() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException ex) {
            // Success
        }
    }

    @Test
    public void testCheckNotNull_pattern_args() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 1);
        } catch (NullArgumentException ex) {
            fail("NullArgumentException not expected");
        }
    }

    @Test
    public void testCheckNotNull_pattern_args_null() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 1);
            fail("NullArgumentException expected");
        } catch (NullArgumentException ex) {
            // Success
        }
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException ex) {
            fail("NullArgumentException not expected");
        }
    }

    @Test
    public void testCheckNotNull_null() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("NullArgumentException expected");
        } catch (NullArgumentException ex) {
            // Success
        }
    }
}