package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        assertEquals(-1509550034, MathUtils.hash(3.14159));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(1072693248, MathUtils.hash(-1.0));
    }
    
    @Test
    public void testHash_doubleArray() {
        assertEquals(-841780783, MathUtils.hash(new double[] { 1.0, 2.0, 3.0 }));
        assertEquals(0, MathUtils.hash(new double[] {}));
        assertEquals(-187503681, MathUtils.hash(new double[] { -1.0, -2.0, -3.0 }));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(2.356194490192345, MathUtils.normalizeAngle(12.566370614359172, 0.0), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 6.283185307179586, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.reduce(6.283185307179586, 6.283185307179586, 0.0), 0.0);
        assertEquals(2.356194490192345, MathUtils.reduce(12.566370614359172, 6.283185307179586, 0.0), 0.0);
    }
    
    @Test
    public void testCopySign_byte() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) -128, MathUtils.copySign((byte) 128, (byte) 1));
    }
    
    @Test
    public void testCopySign_short() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) -32768, MathUtils.copySign((short) 32768, (short) 1));
    }
    
    @Test
    public void testCopySign_int() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(Integer.MIN_VALUE, MathUtils.copySign(Integer.MIN_VALUE, 1));
    }
    
    @Test
    public void testCopySign_long() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(Long.MIN_VALUE, MathUtils.copySign(Long.MIN_VALUE, 1L));
    }
    
    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(1.0);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_Infinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_NaN() {
        MathUtils.checkFinite(Double.NaN);
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        MathUtils.checkFinite(new double[] { 1.0, 2.0, 3.0 });
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_Infinite() {
        MathUtils.checkFinite(new double[] { 1.0, Double.POSITIVE_INFINITY, 3.0 });
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_NaN() {
        MathUtils.checkFinite(new double[] { 1.0, Double.NaN, 3.0 });
    }
    
    @Test
    public void testCheckNotNull_Localizable() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.OVERFLOW);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Localizable_Null() {
        MathUtils.checkNotNull(null, LocalizedFormats.OVERFLOW);
    }
    
    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull(new Object());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Null() {
        MathUtils.checkNotNull(null);
    }
    
}