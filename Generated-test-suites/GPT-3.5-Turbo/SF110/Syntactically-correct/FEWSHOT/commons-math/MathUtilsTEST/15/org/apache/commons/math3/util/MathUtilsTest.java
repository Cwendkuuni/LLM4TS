package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(1072693244, MathUtils.hash(3.14));
        assertEquals(1072693244, MathUtils.hash(-3.14));
    }

    @Test
    public void testHash_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {3.0, 2.0, 1.0};
        
        assertEquals(array1.hashCode(), MathUtils.hash(array1));
        assertEquals(array1.hashCode(), MathUtils.hash(array2));
        assertNotEquals(array1.hashCode(), MathUtils.hash(array3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.000001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 0.000001);
        assertEquals(0.785398163397448, MathUtils.normalizeAngle(-5.497787143782138, 2.356194490192345), 0.000001);
        assertEquals(5.497787143782138, MathUtils.normalizeAngle(1.570796326794897, -3.9269908169872414), 0.000001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(6.283185307179586, 6.283185307179586, 0.0), 0.000001);
        assertEquals(3.0, MathUtils.reduce(10.0, 7.0, 2.0), 0.000001);
        assertEquals(-4.0, MathUtils.reduce(-11.5, 7.0, 2.0), 0.000001);
        assertEquals(41.5, MathUtils.reduce(-125.0, 7.0, -1.5), 0.000001);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte() throws MathArithmeticException {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)1));
        assertEquals((byte)-5, MathUtils.copySign((byte)5, (byte)-1));
        assertEquals((byte)5, MathUtils.copySign((byte)-5, (byte)-1));
        MathUtils.copySign((byte)-128, (byte)1);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short() throws MathArithmeticException {
        assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
        assertEquals((short)-5, MathUtils.copySign((short)5, (short)-1));
        assertEquals((short)5, MathUtils.copySign((short)-5, (short)-1));
        MathUtils.copySign((short)-32768, (short)1);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int() throws MathArithmeticException {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(5, MathUtils.copySign(-5, -1));
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
        assertEquals(5L, MathUtils.copySign(-5L, -1L));
        MathUtils.copySign(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_Exception() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_Exception2() {
        MathUtils.checkFinite(Double.NaN);
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {-1.0, -2.0, -3.0};
        
        MathUtils.checkFinite(array1);
        MathUtils.checkFinite(array2);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_Exception() {
        double[] array = {1.0, 2.0, Double.POSITIVE_INFINITY, 4.0};
        
        MathUtils.checkFinite(array);
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        MathUtils.checkNotNull(this, null);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Localizable_ObjectArray_Exception() {
        MathUtils.checkNotNull(null, null);
    }
    
    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull(this);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Exception() {
        MathUtils.checkNotNull(null);
    }
}