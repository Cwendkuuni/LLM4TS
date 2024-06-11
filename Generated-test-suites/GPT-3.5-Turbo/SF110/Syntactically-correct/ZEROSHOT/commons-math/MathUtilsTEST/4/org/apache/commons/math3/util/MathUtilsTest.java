package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.Localizable;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(12345678, MathUtils.hash(12345));
        assertEquals(-87654321, MathUtils.hash(-87654));
        assertEquals(0, MathUtils.hash(0));
        assertEquals(0, MathUtils.hash(0.0));
    }
    
    @Test
    public void testHashArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {3.0, 2.0, 1.0};
        double[] array3 = {1.0, 2.0, 3.0};
        
        assertEquals(12345678, MathUtils.hash(array1));
        assertNotEquals(MathUtils.hash(array1), MathUtils.hash(array2));
        assertEquals(MathUtils.hash(array1), MathUtils.hash(array3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(7.0, Math.PI), 0.0);
        assertEquals(3.0, MathUtils.normalizeAngle(-3.0, 2.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 1.0), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(7.0, Math.PI, 0.0), 0.0);
        assertEquals(-1.0, MathUtils.reduce(-3.0, 2.0, 0.0), 0.0);
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.1), 0.0);
    }
    
    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals(10, MathUtils.copySign((byte)10, (byte)-5));
        assertEquals(-20, MathUtils.copySign((byte)-20, (byte)15));
        
        try {
            MathUtils.copySign((byte)-128, (byte)5);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals(100, MathUtils.copySign((short)100, (short)-50));
        assertEquals(-200, MathUtils.copySign((short)-200, (short)150));
        
        try {
            MathUtils.copySign((short)-32768, (short)5);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(1000, MathUtils.copySign(1000, -500));
        assertEquals(-2000, MathUtils.copySign(-2000, 1500));
        
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 5);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(1000000000L, MathUtils.copySign(1000000000L, -500000000L));
        assertEquals(-2000000000L, MathUtils.copySign(-2000000000L, 1500000000L));
        
        try {
            MathUtils.copySign(Long.MIN_VALUE, 5L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(1234.5);
        MathUtils.checkFinite(-5678.9);
        MathUtils.checkFinite(0.0);
        
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, Double.POSITIVE_INFINITY, 3.0};
        double[] array3 = {1.0, 2.0, Double.NaN};
        
        MathUtils.checkFinite(array1);
        
        try {
            MathUtils.checkFinite(array2);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
        
        try {
            MathUtils.checkFinite(array3);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        MathUtils.checkNotNull("Hello", null, null);
        
        try {
            MathUtils.checkNotNull(null, Localizable.NONE, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        MathUtils.checkNotNull("Hello");
        
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }
}