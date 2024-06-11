package org.apache.commons.math3.util;

import org.junit.*;
import java.util.Arrays;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class MathUtilsTest {

    @Test
    public void testHashWithSingleValue() {
        double value = 2.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashWithArray() {
        double[] value = {1.2, 3.4, 5.6};
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
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 1;
        byte sign = -1;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 1000;
        short sign = -1;
        short expected = -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 100000;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000000000L;
        long sign = -1L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteWithFiniteNumber() {
        double x = 5.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Should not throw NotFiniteNumberException");
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfiniteNumber() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteArrayWithFiniteNumberArray() {
        double[] val = {1.2, 3.4, 5.6};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Should not throw NotFiniteNumberException");
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithInfiniteNumberArray() {
        double[] val = {Double.POSITIVE_INFINITY, 3.4, 5.6};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithNaNArray() {
        double[] val = {1.2, Double.NaN, 5.6};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("Should not throw NullArgumentException");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test
    public void testCheckNotNullWithNonNullObjectAndPattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = new Object[0];
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            Assert.fail("Should not throw NullArgumentException");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObjectAndPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = new Object[0];
        MathUtils.checkNotNull(o, pattern, args);
    }

}