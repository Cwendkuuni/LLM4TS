package org.apache.commons.math3.util;

import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    // Test cases for hash(double value) method
    
    @Test
    public void testHashWithPositiveValue() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashWithNegativeValue() {
        double value = -5.2;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    // Test cases for hash(double[] value) method
    
    @Test
    public void testHashWithEmptyArray() {
        double[] value = {};
        int expected = 0;
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashWithNonEmptyArray() {
        double[] value = {1.2, -3.4, 5.6};
        int expected = Double.valueOf(value[0]).hashCode() ^ Double.valueOf(value[1]).hashCode() ^ Double.valueOf(value[2]).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    // Test cases for normalizeAngle(double a, double center) method
    
    @Test
    public void testNormalizeAngleWithPositiveAAndCenter() {
        double a = 4.0;
        double center = 2.0;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testNormalizeAngleWithNegativeAAndCenter() {
        double a = -4.0;
        double center = -2.0;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.0001);
    }
    
    // Test cases for reduce(double a, double period, double offset) method
    
    @Test
    public void testReduceWithPositiveAAndPeriodWithOffset() {
        double a = 10.5;
        double period = 5.0;
        double offset = 2.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testReduceWithNegativeAAndPeriodWithOffset() {
        double a = -7.5;
        double period = 5.0;
        double offset = -2.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.0001);
    }
    
    // Test cases for copySign(byte magnitude, byte sign) method
    
    @Test
    public void testCopySignWithPositiveMagnitudeAndSign() {
        byte magnitude = 10;
        byte sign = 8;
        byte expected = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySignWithNegativeMagnitudeAndSign() {
        byte magnitude = -5;
        byte sign = -3;
        byte expected = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithPositiveSignAndMagnitudeMinValue() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    // Test cases for copySign(short magnitude, short sign) method
    
    @Test
    public void testCopySignWithPositiveMagnitudeAndSign() {
        short magnitude = 1000;
        short sign = 800;
        short expected = magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySignWithNegativeMagnitudeAndSign() {
        short magnitude = -2000;
        short sign = -1500;
        short expected = magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithPositiveSignAndMagnitudeMinValue() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    // Test cases for copySign(int magnitude, int sign) method
    
    @Test
    public void testCopySignWithPositiveMagnitudeAndSign() {
        int magnitude = 100000;
        int sign = 80000;
        int expected = magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySignWithNegativeMagnitudeAndSign() {
        int magnitude = -200000;
        int sign = -150000;
        int expected = magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithPositiveSignAndMagnitudeMinValue() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    // Test cases for copySign(long magnitude, long sign) method
    
    @Test
    public void testCopySignWithPositiveMagnitudeAndSign() {
        long magnitude = 100000000000L;
        long sign = 80000000000L;
        long expected = magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySignWithNegativeMagnitudeAndSign() {
        long magnitude = -200000000000L;
        long sign = -150000000000L;
        long expected = magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithPositiveSignAndMagnitudeMinValue() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    // Test cases for checkFinite(double x) method
    
    @Test
    public void testCheckFiniteWithFiniteNumber() {
        double x = 3.14;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithPositiveInfinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNegativeInfinity() {
        double x = Double.NEGATIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    // Test cases for checkFinite(double[] val) method
    
    @Test
    public void testCheckFiniteArrayWithFiniteNumbers() {
        double[] val = {1.2, -3.4, 5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithPositiveInfinity() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, 5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithNegativeInfinity() {
        double[] val = {1.2, Double.NEGATIVE_INFINITY, 5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithNaN() {
        double[] val = {1.2, Double.NaN, 5.6};
        MathUtils.checkFinite(val);
    }
    
    // Test cases for checkNotNull(Object o, Localizable pattern, Object... args) method
    
    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObjectAndNullPattern() {
        Object o = null;
        MathUtils.checkNotNull(o, null);
    }
    
    @Test
    public void testCheckNotNullWithNullObjectAndNonNullPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
        try {
            MathUtils.checkNotNull(o, pattern);
        } catch (NullArgumentException e) {
            assertEquals(pattern, e.getPattern());
        }
    }
    
    // Test cases for checkNotNull(Object o) method
    
    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}