package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 7.5;
        double center = 3.5;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte() {
        byte magnitude = -128;
        byte sign = 1;
        byte actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_byte2() {
        byte magnitude = -1;
        byte sign = -1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short() {
        short magnitude = -32768;
        short sign = 1;
        short actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short2() {
        short magnitude = -1;
        short sign = -1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        int actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int2() {
        int magnitude = -1;
        int sign = -1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        long actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long2() {
        long magnitude = -1L;
        long sign = -1L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double2() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray2() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull_Localizable() {
        Object o = new Object();
        Localizable pattern = new DummyLocalizable();
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Localizable2() {
        Object o = null;
        Localizable pattern = new DummyLocalizable();
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object2() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}