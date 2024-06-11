package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.Localizable;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash_double() {
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(Double.hashCode(1.0), MathUtils.hash(1.0));
        assertEquals(Double.hashCode(-1.0), MathUtils.hash(-1.0));
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] array1 = {};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {3.0, 2.0, 1.0};
        
        assertEquals(0, MathUtils.hash(array1));
        assertEquals(Arrays.hashCode(array2), MathUtils.hash(array2));
        assertEquals(Arrays.hashCode(array3), MathUtils.hash(array3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(2.0, MathUtils.normalizeAngle(8.0, 6.0), 0.0);
        assertEquals(1.5, MathUtils.normalizeAngle(7.5, 2.5), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.0), 0.0);
        assertEquals(0.0, MathUtils.reduce(10.0, 5.0, 5.0), 0.0);
        assertEquals(-1.0, MathUtils.reduce(-5.0, 4.0, 4.0), 0.0);
    }
    
    @Test
    public void testCopySign_byte() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign((byte)0, (byte)1));
        assertEquals(-1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals(1, MathUtils.copySign((byte)1, (byte)-1));
        assertEquals(-1, MathUtils.copySign((byte)-1, (byte)-1));
        
        try {
            MathUtils.copySign((byte)-128, (byte)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCopySign_short() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign((short)0, (short)1));
        assertEquals(-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals(1, MathUtils.copySign((short)1, (short)-1));
        assertEquals(-1, MathUtils.copySign((short)-1, (short)-1));
        
        try {
            MathUtils.copySign((short)-32768, (short)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCopySign_int() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign(0, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, -1));
        
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals(0L, MathUtils.copySign(0L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(1L, MathUtils.copySign(1L, -1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
        
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCheckFinite_double() throws NotFiniteNumberException {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
        
        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
        
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() throws NotFiniteNumberException {
        double[] array1 = {0.0, 1.0, -1.0};
        double[] array2 = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        double[] array3 = {1.0, Double.NaN, 3.0};
        
        MathUtils.checkFinite(array1);
        MathUtils.checkFinite(array2);
        MathUtils.checkFinite(array3);
        
        try {
            MathUtils.checkFinite(new double[]{1.0, 2.0, Double.POSITIVE_INFINITY});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
        
        try {
            MathUtils.checkFinite(new double[]{Double.NaN, 2.0, 3.0});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() throws NullArgumentException {
        Object obj1 = new Object();
        Object obj2 = null;
        
        MathUtils.checkNotNull(obj1, null);
        MathUtils.checkNotNull(obj2, null);
        
        try {
            MathUtils.checkNotNull(obj2, (LocalizedFormats)null, (Object[])null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testCheckNotNull_Object() throws NullArgumentException {
        Object obj1 = new Object();
        Object obj2 = null;
        
        MathUtils.checkNotNull(obj1);
        MathUtils.checkNotNull(obj2);
    }
}