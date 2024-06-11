package org.apache.commons.math3.util;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 1.234;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.234, 5.678, 9.012};
        int expected = value.hashCode();
        int result = MathUtils.hash(value);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 2.5;
        double center = 0.5;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double result = MathUtils.normalizeAngle(a, center);
        
        Assert.assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        
        Assert.assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte) (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude);
        
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -50;
        short expected = (short) (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude);
        
        try {
            short result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -500;
        int expected = magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude;
        
        try {
            int result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 100000L;
        long sign = -50000L;
        long expected = magnitude >= 0L && sign >= 0L || magnitude < 0L && sign < 0L ? magnitude : -magnitude;
        
        try {
            long result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        
        try {
            MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCheckFinite_double_finite() {
        double x = 1.234;
        
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_double_infinite() {
        double x = Double.POSITIVE_INFINITY;
        
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(x, e.getNumber(), 0.0001);
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCheckFinite_double_nan() {
        double x = Double.NaN;
        
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(x, e.getNumber(), 0.0001);
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_finite() {
        double[] val = {1.234, 5.678, 9.012};
        
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_infinite() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 2.0};
        
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            Assert.assertEquals(Double.POSITIVE_INFINITY, e.getNumber(), 0.0001);
            Assert.assertArrayEquals(new Object[] {1}, e.getArguments());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_nan() {
        double[] val = {1.0, Double.NaN, 2.0};
        
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            Assert.assertEquals(Double.NaN, e.getNumber(), 0.0001);
            Assert.assertArrayEquals(new Object[] {1}, e.getArguments());
        }
    }
    
    @Test
    public void testCheckNotNull_Object_pattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
        Object[] args = new Object[] {"arg"};
        
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Object_pattern_null() {
        Object o = null;
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
        Object[] args = new Object[] {"arg"};
        
        try {
            MathUtils.checkNotNull(o, pattern, args);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            Assert.assertEquals(pattern, e.getPattern());
            Assert.assertArrayEquals(args, e.getArguments());
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Object_null() {
        Object o = null;
        
        try {
            MathUtils.checkNotNull(o);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            Assert.assertNull(e.getPattern());
            Assert.assertArrayEquals(new Object[0], e.getArguments());
        }
    }
}