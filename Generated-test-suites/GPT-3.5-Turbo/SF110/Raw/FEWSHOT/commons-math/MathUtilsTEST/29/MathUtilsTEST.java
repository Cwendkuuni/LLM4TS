package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        assertEquals(1234567, MathUtils.hash(1234567.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-1234567, MathUtils.hash(-1234567.0));
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {1.0, 2.0, 4.0};
        
        assertEquals(array1.hashCode(), MathUtils.hash(array1));
        assertEquals(array2.hashCode(), MathUtils.hash(array2));
        assertNotEquals(array1.hashCode(), MathUtils.hash(array3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(0.0, 3.141592653589793), 0.0);
        assertEquals(0.7853981633974483, MathUtils.normalizeAngle(7.068583470577034, 0.0), 0.0);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(4.71238898038469, 3.141592653589793), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(4.0, MathUtils.reduce(14.0, 5.0, 1.0), 0.0);
        assertEquals(-2.0, MathUtils.reduce(-12.0, 5.0, -1.0), 0.0);
        assertEquals(5.0, MathUtils.reduce(5.0, 1.0, 0.0), 0.0);
        assertEquals(3.0, MathUtils.reduce(15.0, 5.0, 0.0), 0.0);
    }
    
    @Test
    public void testCopySign_byte() {
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 1));
        assertEquals((byte) -5, MathUtils.copySign((byte) 5, (byte) -1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        MathUtils.copySign((byte) -128, (byte) 1);
    }
    
    @Test
    public void testCopySign_short() {
        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 1));
        assertEquals((short) -5, MathUtils.copySign((short) 5, (short) -1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        MathUtils.copySign((short) -32768, (short) 1);
    }
    
    @Test
    public void testCopySign_int() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }
    
    @Test
    public void testCopySign_long() {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        MathUtils.copySign(Long.MIN_VALUE, 1L);
    }
    
    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(123.45);
        MathUtils.checkFinite(-1.2345);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_infinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_nan() {
        MathUtils.checkFinite(Double.NaN);
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, Double.POSITIVE_INFINITY, 3.0};
        
        MathUtils.checkFinite(array1);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_infinite() {
        double[] array = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(array);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_nan() {
        double[] array = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(array);
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable() {
        Object obj1 = new Object();
        Object obj2 = null;
        
        MathUtils.checkNotNull(obj1);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Localizable_nullObject() {
        MathUtils.checkNotNull(null, null);
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object obj1 = new Object();
        Object obj2 = null;
        
        MathUtils.checkNotNull(obj1);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_nullObject() {
        MathUtils.checkNotNull(null);
    }

}