package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.Localizable;
import org.junit.*;
import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHash_singleValue() {
        double value = 3.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHash_arrayValue() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 7.0;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 5;
        byte sign = 10;
        try {
            byte expected = (byte) (-magnitude);
            byte actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 10;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -50;
        try {
            short expected = (short) (-magnitude);
            short actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 10;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -500;
        try {
            int expected = -magnitude;
            int actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 10;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 100000;
        long sign = -50000;
        try {
            long expected = -magnitude;
            long actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 10;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFinite_singleValue() {
        double x = 5.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCheckFinite_singleValue_infinite() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFinite_singleValue_nan() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFinite_arrayValue() {
        double[] val = { 1.0, 2.0, 3.0 };
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCheckFinite_arrayValue_infinite() {
        double[] val = { 1.0, 2.0, Double.POSITIVE_INFINITY };
        try {
            MathUtils.checkFinite(val);
            Assert.fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFinite_arrayValue_nan() {
        double[] val = { 1.0, 2.0, Double.NaN };
        try {
            MathUtils.checkFinite(val);
            Assert.fail("NotFiniteNumberException expected");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckNotNull_withPattern() {
        Object o = new Object();
        Localizable pattern = null;
        Object[] args = null;
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCheckNotNull_withPattern_nullObject() {
        Object o = null;
        Localizable pattern = null;
        Object[] args = null;
        try {
            MathUtils.checkNotNull(o, pattern, args);
            Assert.fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckNotNull_noPattern() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("No exception expected");
        }
    }

    @Test
    public void testCheckNotNull_noPattern_nullObject() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            Assert.fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }

}