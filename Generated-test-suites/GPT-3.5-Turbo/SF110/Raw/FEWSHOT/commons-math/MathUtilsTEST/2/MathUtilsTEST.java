package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(123456789, MathUtils.hash(123456789.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-123456789, MathUtils.hash(-123456789.0));
    }
    
    @Test
    public void testHash_array() {
        assertEquals(123456789, MathUtils.hash(new double[] {123456789.0}));
        assertEquals(0, MathUtils.hash(new double[] {0.0}));
        assertEquals(-123456789, MathUtils.hash(new double[] {-123456789.0}));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(9.42477796076938, 3.141592653589793), 0.0);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(7.853981633974483, 1.5707963267948966), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(6.283185307179586, 6.283185307179586, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.reduce(9.42477796076938, 6.283185307179586, 0.0), 0.0);
        assertEquals(1.5707963267948966, MathUtils.reduce(7.853981633974483, 3.141592653589793, 0.0), 0.0);
    }
    
    @Test
    public void testCopySign_byte() throws MathArithmeticException {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) 1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) -1));
        
        try {
            MathUtils.copySign((byte) -128, (byte) 1);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCopySign_short() throws MathArithmeticException {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) 1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) -1));
        
        try {
            MathUtils.copySign((short) -32768, (short) 1);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCopySign_int() throws MathArithmeticException {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, -1));
        
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
        
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(123.456);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(-987.654);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_inf() throws NotFiniteNumberException {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_nan() throws NotFiniteNumberException {
        MathUtils.checkFinite(Double.NaN);
    }
    
    @Test
    public void testCheckFinite_array() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[] {123.456});
        MathUtils.checkFinite(new double[] {0.0});
        MathUtils.checkFinite(new double[] {-987.654});
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_array_inf() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[] {Double.POSITIVE_INFINITY});
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_array_nan() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[] {Double.NaN});
    }
    
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        MathUtils.checkNotNull(123);
        MathUtils.checkNotNull("abc");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null_pattern() throws NullArgumentException {
        MathUtils.checkNotNull(null, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null() throws NullArgumentException {
        MathUtils.checkNotNull(null);
    }
}