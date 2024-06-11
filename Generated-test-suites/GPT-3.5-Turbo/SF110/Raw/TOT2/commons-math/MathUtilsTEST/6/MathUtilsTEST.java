package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;


public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] valueArray = {3.14, 6.28};
        int expected = Arrays.hashCode(valueArray);
        int actual = MathUtils.hash(valueArray);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 5.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
        
        // Test overflow exception
        magnitude = -128;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(magnitude, sign);
        });
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -50;
        short expected = (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
        
        // Test overflow exception
        magnitude = -32768;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(magnitude, sign);
        });
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -500;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
        
        // Test overflow exception
        magnitude = Integer.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(magnitude, sign);
        });
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 100000L;
        long sign = -50000L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
        
        // Test overflow exception
        magnitude = Long.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(magnitude, sign);
        });
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 10.5;
        assertDoesNotThrow(() -> {
            MathUtils.checkFinite(x);
        });
        
        // Test NotFiniteNumberException
        x = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(x);
        });
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {10.5, Double.NaN};
        assertDoesNotThrow(() -> {
            MathUtils.checkFinite(val);
        });
        
        // Test NotFiniteNumberException
        double[] inval = {10.5, Double.POSITIVE_INFINITY};
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(inval);
        });
    }
    
    @Test
    public void testCheckNotNull_withPattern() {
        Object o = new Object();
        Localizable pattern = new DummyLocalizable("Object cannot be null");
        assertDoesNotThrow(() -> {
            MathUtils.checkNotNull(o, pattern);
        });
        
        // Test NullArgumentException
        Object nullObject = null;
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(nullObject, pattern);
        });
    }
    
    @Test
    public void testCheckNotNull_withoutPattern() {
        Object o = new Object();
        assertDoesNotThrow(() -> {
            MathUtils.checkNotNull(o);
        });
        
        // Test NullArgumentException
        Object nullObject = null;
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(nullObject);
        });
    }
}