package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MathUtilsTest {

    // Test for hash(double value)
    @Test
    public void testHash() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    // Test for hash(double[] value)
    @Test
    public void testHashArray() {
        double[] value = {10.5, 20.3, 30.7};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    // Test for normalizeAngle(double a, double center)
    @Test
    public void testNormalizeAngle() {
        double a = 1.2;
        double center = 0.5;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    // Test for reduce(double a, double period, double offset)
    @Test
    public void testReduce() {
        double a = 7.8;
        double period = 2.5;
        double offset = 1.5;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    // Test for copySign(byte magnitude, byte sign)
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte) (-magnitude);
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail(e.getMessage());
        }
    }

    // Test for copySign(short magnitude, short sign)
    @Test
    public void testCopySignShort() {
        short magnitude = 30000;
        short sign = -10000;
        short expected = (short) (-magnitude);
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail(e.getMessage());
        }
    }

    // Test for copySign(int magnitude, int sign)
    @Test
    public void testCopySignInt() {
        int magnitude = 500000;
        int sign = -100000;
        int expected = -magnitude;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail(e.getMessage());
        }
    }

    // Test for copySign(long magnitude, long sign)
    @Test
    public void testCopySignLong() {
        long magnitude = 2000000000;
        long sign = -4000000000L;
        long expected = -magnitude;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail(e.getMessage());
        }
    }

    // Test for checkFinite(double x)
    @Test
    public void testCheckFinite() {
        double x = 10.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail(e.getMessage());
        }
    }

    // Test for checkFinite(double[] val)
    @Test
    public void testCheckFiniteArray() {
        double[] val = {10.5, 20.3, Double.NaN};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getMessage());
        }
    }

    // Test for checkNotNull(Object o, Localizable pattern, Object... args)
    @Test
    public void testCheckNotNullPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {0};
        try {
            MathUtils.checkNotNull(o, pattern, args);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            assertEquals(pattern, e.getMessage());
        }
    }

    // Test for checkNotNull(Object o)
    @Test
    public void testCheckNotNull() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            assertEquals(null, e.getMessage());
        }
    }

}