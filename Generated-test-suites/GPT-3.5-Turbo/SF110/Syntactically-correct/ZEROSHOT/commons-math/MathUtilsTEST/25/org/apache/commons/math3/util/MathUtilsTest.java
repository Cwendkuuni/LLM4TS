
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
    public void testHash() {
        double value = 3.14;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashArray() {
        double[] value = {1.23, 4.56, 7.89};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 2.0;
        double center = 1.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 3.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = -magnitude;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 1000;
        short sign = -500;
        short expected = -magnitude;
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 100000;
        int sign = -50000;
        int expected = -magnitude;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000000000L;
        long sign = -5000000000L;
        long expected = -magnitude;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCheckFinite() {
        double x = 1.234;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(x, e.getArgument());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(x, e.getArgument());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.23, 4.56, 7.89};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteArrayElement() {
        double[] val = {1.23, Double.POSITIVE_INFINITY, 7.89};
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecifier());
            Assert.assertEquals(val[1], e.getArgument());
            Assert.assertArrayEquals(new Object[] { 1 }, e.getArguments());
        }
    }

    @Test
    public void testCheckNotNullLocalizable() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.SOME_PATTERN;
        Object[] args = {1, 2, 3};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNullLocalizableNull() {
        Object o = null;
        Localizable pattern = LocalizedFormats.SOME_PATTERN;
        Object[] args = {1, 2, 3};
        try {
            MathUtils.checkNotNull(o, pattern, args);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            Assert.assertEquals(pattern, e.getSpecifier());
            Assert.assertArrayEquals(args, e.getArguments());
        }
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNullNull() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            Assert.assertNull(e.getSpecifier());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
}
