package org.apache.commons.math3.util;

import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 2.5;
        assertEquals("Test hash(double)", Double.valueOf(value).hashCode(), MathUtils.hash(value));
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {2.5, 3.7, 4.2};
        assertEquals("Test hash(double[])", Arrays.hashCode(value), MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        assertEquals("Test normalizeAngle()", a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586), MathUtils.normalizeAngle(a, center), 0);
    }

    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        assertEquals("Test reduce()", a - p * FastMath.floor((a - offset) / p) - offset, MathUtils.reduce(a, period, offset), 0);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 5;
        byte sign = 2;
        try {
            assertEquals("Test copySign(byte)", (byte) (-magnitude), MathUtils.copySign(magnitude, sign));
        } catch (MathArithmeticException e) {
            assertEquals("Test copySign(byte) - Overflow Exception", LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 5;
        short sign = 2;
        try {
            assertEquals("Test copySign(short)", (short) (-magnitude), MathUtils.copySign(magnitude, sign));
        } catch (MathArithmeticException e) {
            assertEquals("Test copySign(short) - Overflow Exception", LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 5;
        int sign = 2;
        try {
            assertEquals("Test copySign(int)", -magnitude, MathUtils.copySign(magnitude, sign));
        } catch (MathArithmeticException e) {
            assertEquals("Test copySign(int) - Overflow Exception", LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 5L;
        long sign = 2L;
        try {
            assertEquals("Test copySign(long)", -magnitude, MathUtils.copySign(magnitude, sign));
        } catch (MathArithmeticException e) {
            assertEquals("Test copySign(long) - Overflow Exception", LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 5.0;
        try {
            MathUtils.checkFinite(x);
            assertTrue("Test checkFinite(double) - Success", true);
        } catch (NotFiniteNumberException e) {
            assertTrue("Test checkFinite(double) - NotFiniteNumberException", false);
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {2.5, 3.7, 4.2};
        try {
            MathUtils.checkFinite(val);
            assertTrue("Test checkFinite(double[]) - Success", true);
        } catch (NotFiniteNumberException e) {
            assertTrue("Test checkFinite(double[]) - NotFiniteNumberException", false);
        }
    }

    @Test
    public void testCheckNotNull_Object_Localizable_varargs() {
        Object o = new Object();
        Localizable pattern = null;
        Object[] args = null;
        try {
            MathUtils.checkNotNull(o, pattern, args);
            assertTrue("Test checkNotNull(Object, Localizable, Object[]) - Success", true);
        } catch (NullArgumentException e) {
            assertTrue("Test checkNotNull(Object, Localizable, Object[]) - NullArgumentException", false);
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
            assertTrue("Test checkNotNull(Object) - Success", true);
        } catch (NullArgumentException e) {
            assertTrue("Test checkNotNull(Object) - NullArgumentException", false);
        }
    }
}