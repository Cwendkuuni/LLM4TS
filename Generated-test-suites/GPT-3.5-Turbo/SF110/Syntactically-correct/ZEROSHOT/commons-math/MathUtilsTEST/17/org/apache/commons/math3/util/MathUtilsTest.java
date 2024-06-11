package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.LocalizedFormats;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.5, 2.5, 3.5};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.4;
        double center = 1.2;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 5.6;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 2;
        byte sign = -1;
        byte expected = -magnitude;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 32767;
        short sign = -1;
        short expected = -magnitude;
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 2147483647;
        int sign = -1;
        int expected = -magnitude;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 9223372036854775807L;
        long sign = -1L;
        long expected = -magnitude;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 3.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFinite_double_Infinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckFinite_double_NaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.5, 2.5, 3.5};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFinite_doubleArray_Infinity() {
        double[] val = {1.5, Double.POSITIVE_INFINITY, 3.5};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCheckFinite_doubleArray_NaN() {
        double[] val = {1.5, Double.NaN, 3.5};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull_Object_null() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNull_Object_Localizable() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.PATTERN;
        Object[] args = {1};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull_Object_Localizable_null() {
        Object o = null;
        Localizable pattern = LocalizedFormats.PATTERN;
        Object[] args = {1};
        try {
            MathUtils.checkNotNull(o, pattern, args);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals(pattern.getMessage(args), e.getMessage());
        }
    }
}