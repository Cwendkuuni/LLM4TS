
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 1.234;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.234, 5.678};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 7.5;
        double center = 2.5;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 7.5;
        double period = 2.0;
        double offset = 1.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 1;
        byte sign = -1;
        byte expected = (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 1;
        short sign = -1;
        short expected = (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 1;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 1L;
        long sign = -1L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 1.234;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_infinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_nan() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.234, 5.678};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_infinity() {
        double[] val = {1.234, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_nan() {
        double[] val = {1.234, Double.NaN};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull_LocalizableObjectArray() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_LocalizableObjectArray_null() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_null() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}
