package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class MathUtilsTest {
    
    @Before
    public void setUp() {
        // Setup code goes here
    }
    
    @Test
    public void testHash() {
        double value = 1.0;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 3.0;
        double offset = 1.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 1;
        byte sign = -1;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 1;
        short sign = -1;
        short expected = -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = 1;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = 1L;
        long sign = -1L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        double x = 1.0;
        // No exception expected
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() throws NotFiniteNumberException {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() throws NotFiniteNumberException {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] val = {1.0, 2.0, 3.0};
        // No exception expected
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() throws NotFiniteNumberException {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() throws NotFiniteNumberException {
        double[] val = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        Object o = new Object();
        // No exception expected
        MathUtils.checkNotNull(o, LocalizedFormats.PATTERN, "argument");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPatternNull() throws NullArgumentException {
        MathUtils.checkNotNull(null, LocalizedFormats.PATTERN, "argument");
    }
    
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object o = new Object();
        // No exception expected
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() throws NullArgumentException {
        MathUtils.checkNotNull(null);
    }
    
}