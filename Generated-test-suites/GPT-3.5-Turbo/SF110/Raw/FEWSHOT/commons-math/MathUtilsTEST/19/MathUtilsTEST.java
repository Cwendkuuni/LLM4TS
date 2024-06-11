
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(Double.valueOf(0).hashCode(), MathUtils.hash(0.0));
        assertEquals(Double.valueOf(1).hashCode(), MathUtils.hash(1.0));
        assertEquals(Double.valueOf(2).hashCode(), MathUtils.hash(2.0));
        assertEquals(Double.valueOf(3).hashCode(), MathUtils.hash(3.0));
    }
    
    @Test
    public void testHashArray() {
        double[] values = {0.0, 1.0, 2.0, 3.0};
        int expectedHash = Arrays.hashCode(values);
        assertEquals(expectedHash, MathUtils.hash(values));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(-0.3, MathUtils.normalizeAngle(6.0, 5.7), 0.001);
        assertEquals(0.1, MathUtils.normalizeAngle(-6.0, 6.1), 0.001);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 6.283185307179586), 0.001);
        assertEquals(3.15, MathUtils.normalizeAngle(-3.13, 0.02), 0.001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(7.0, 6.0, 0.0), 0.001);
        assertEquals(-1.0, MathUtils.reduce(-5.0, 6.0, 0.0), 0.001);
        assertEquals(-0.5, MathUtils.reduce(1.5, 2.0, -0.5), 0.001);
        assertEquals(0.0, MathUtils.reduce(0.0, 2.0, -1.0), 0.001);
    }
    
    @Test
    public void testCopySignByte() {
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 1));
        assertEquals((byte) -5, MathUtils.copySign((byte) 5, (byte) -1));
        assertEquals((byte) -5, MathUtils.copySign((byte) -5, (byte) 1));
        assertEquals((byte) 5, MathUtils.copySign((byte) -5, (byte) -1));
        
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign((byte) -128, (byte) 1);
        });
    }
    
    @Test
    public void testCopySignShort() {
        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 1));
        assertEquals((short) -5, MathUtils.copySign((short) 5, (short) -1));
        assertEquals((short) -5, MathUtils.copySign((short) -5, (short) 1));
        assertEquals((short) 5, MathUtils.copySign((short) -5, (short) -1));
        
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign((short) -32768, (short) 1);
        });
    }
    
    @Test
    public void testCopySignInt() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(-5, MathUtils.copySign(-5, 1));
        assertEquals(5, MathUtils.copySign(-5, -1));
        
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
        });
    }
    
    @Test
    public void testCopySignLong() {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
        assertEquals(-5L, MathUtils.copySign(-5L, 1L));
        assertEquals(5L, MathUtils.copySign(-5L, -1L));
        
        assertThrows(MathArithmeticException.class, () -> {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
        });
    }
    
    @Test
    public void testCheckFinite() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        });
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        });
        
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(Double.NaN);
        });
    }
    
    @Test
    public void testCheckFiniteArray() {
        double[] values1 = {0.0, 1.0, -1.0};
        MathUtils.checkFinite(values1);
        
        double[] values2= {0.0, 1.0, Double.POSITIVE_INFINITY};
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(values2);
        });
        
        double[] values3= {0.0, 1.0, Double.NEGATIVE_INFINITY};
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(values3);
        });
        
        double[] values4= {0.0, 1.0, Double.NaN};
        assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(values4);
        });
    }
    
    @Test
    public void testCheckNotNull() {
        Object obj1 = new Object();
        MathUtils.checkNotNull(obj1);
        
        Object obj2 = null;
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(obj2, LocalizedFormats.FOO, "bar");
        });
        
        assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(obj2);
        });
    }
}
