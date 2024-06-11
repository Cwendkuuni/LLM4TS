package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashWithDouble() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHashWithDoubleArray() {
        double[] value = {1.5, 2.5, 3.5};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 5.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 3.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0);
    }

    @Test
    public void testCopySignWithByte() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = -5;
        byte expected;
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            expected = magnitude;
        } else if (sign >= 0 && magnitude == -128) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        } else {
            expected = (byte) (-magnitude);
        }
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test
    public void testCopySignWithShort() throws MathArithmeticException {
        short magnitude = 100;
        short sign = -50;
        short expected;
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            expected = magnitude;
        } else if (sign >= 0 && magnitude == -32768) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        } else {
            expected = (short) (-magnitude);
        }
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test
    public void testCopySignWithInt() throws MathArithmeticException {
        int magnitude = 1000;
        int sign = -500;
        int expected;
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            expected = magnitude;
        } else if (sign >= 0 && magnitude == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        } else {
            expected = -magnitude;
        }
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test
    public void testCopySignWithLong() throws MathArithmeticException {
        long magnitude = 1000000L;
        long sign = -500000L;
        long expected;
        if ((magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L)) {
            expected = magnitude;
        } else if (sign >= 0L && magnitude == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        } else {
            expected = -magnitude;
        }
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithDouble() throws NotFiniteNumberException {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithDoubleArray() throws NotFiniteNumberException {
        double[] val = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        Object o = null;
        Localizable pattern = null;
        Object[] args = null;
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}