package org.apache.commons.math3.util;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14159;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.3;
        double center = 2.7;
        double expected = a - 6.283185307179586 * org.apache.commons.math3.util.FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 2.0;
        double offset = 1.0;
        double p = org.apache.commons.math3.util.FastMath.abs(period);
        double expected = a - p * org.apache.commons.math3.util.FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 2;
        byte sign = 3;
        byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 2;
        short sign = 3;
        short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 2;
        int sign = 3;
        int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 2L;
        long sign = 3L;
        long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCheckFinite_double() {
        double x = 3.14159;
        try {
            MathUtils.checkFinite(x);
        } catch (org.apache.commons.math3.exception.NotFiniteNumberException e) {
            Assert.fail("checkFinite(double) threw NotFiniteNumberException for finite number");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFinite_double_ThrowsNotFiniteNumberException() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (org.apache.commons.math3.exception.NotFiniteNumberException e) {
            Assert.fail("checkFinite(double[]) threw NotFiniteNumberException for finite numbers");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_ThrowsNotFiniteNumberException() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull_Localizable() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o, org.apache.commons.math3.exception.util.LocalizedFormats.PATTERN, (Object[])null);
        } catch (org.apache.commons.math3.exception.NullArgumentException e) {
            Assert.fail("checkNotNull(Object, Localizable, Object[]) threw NullArgumentException for non-null object");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNull_Localizable_ThrowsNullArgumentException() {
        Object o = null;
        MathUtils.checkNotNull(o, org.apache.commons.math3.exception.util.LocalizedFormats.PATTERN, (Object[])null);
    }

    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (org.apache.commons.math3.exception.NullArgumentException e) {
            Assert.fail("checkNotNull(Object) threw NullArgumentException for non-null object");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNull_Object_ThrowsNullArgumentException() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}