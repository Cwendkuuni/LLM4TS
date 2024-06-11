package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.util.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(123456, MathUtils.hash(123456.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-12, MathUtils.hash(-12.0));
    }
    
    @Test
    public void testHashArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {1.0, 2.0, 4.0};
        
        assertEquals(Arrays.hashCode(array1), MathUtils.hash(array1));
        assertEquals(Arrays.hashCode(array2), MathUtils.hash(array2));
        assertEquals(Arrays.hashCode(array3), MathUtils.hash(array3));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(MathUtils.TWO_PI, 0.0), 0.0);
        assertEquals(5.7595865315812875, MathUtils.normalizeAngle(10.0, 2.0), 0.0);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 2.0, 0.0), 0.0);
        assertEquals(1.0, MathUtils.reduce(4.0, 3.0, 1.0), 0.0);
        assertEquals(-2.0, MathUtils.reduce(-8.0, -5.0, -2.0), 0.0);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        MathUtils.copySign((byte)-128, (byte)1);
    }
    
    @Test
    public void testCopySignByte() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
        assertEquals((byte)1, MathUtils.copySign((byte)-1, (byte)-1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        MathUtils.copySign((short)-32768, (short)1);
    }
    
    @Test
    public void testCopySignShort() {
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
        assertEquals((short)1, MathUtils.copySign((short)-1, (short)-1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }
    
    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(-1, -1));
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        MathUtils.copySign(Long.MIN_VALUE, 1L);
    }
    
    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(-1L, -1L));
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        MathUtils.checkFinite(Double.NaN);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }
    
    @Test
    public void testCheckFiniteValid() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(0.0);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] array = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(array);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() {
        double[] array = {1.0, 2.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(array);
    }
    
    @Test
    public void testCheckFiniteArrayValid() {
        double[] array = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(array);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() {
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull() {
        MathUtils.checkNotNull(null);
    }

}