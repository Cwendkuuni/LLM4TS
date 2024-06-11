package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NullArgumentException;
import java.util.Arrays;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        assertEquals(123456789, MathUtils.hash(123456789.0));
        assertEquals(-987654321, MathUtils.hash(-987654321.0));
        assertEquals(0, MathUtils.hash(0.0));
    }
    
    @Test
    public void testHashArray() {
        assertEquals(0, MathUtils.hash(new double[] {}));
        assertEquals(123456789, MathUtils.hash(new double[] {123456789.0}));
        assertEquals(-987654321, MathUtils.hash(new double[] {-987654321.0}));
        assertEquals(999999999, MathUtils.hash(new double[] {111, 222, 333, 444, 555, 666, 777, 888, 999}));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.000001);
        assertEquals(1.570795, MathUtils.normalizeAngle(1.570795, 0.0), 0.000001);
        assertEquals(5.899645, MathUtils.normalizeAngle(0.0, 1.570795), 0.000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185, 0.0), 0.000001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 2.0, 0.0), 0.000001);
        assertEquals(1.0, MathUtils.reduce(5.0, 4.0, 1.0), 0.000001);
        assertEquals(2.0, MathUtils.reduce(2.0, 2.0, 0.0), 0.000001);
        assertEquals(1.0, MathUtils.reduce(11.0, 4.0, 1.0), 0.000001);
    }
    
    @Test
    public void testCopySignByte() {
        try {
            assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)1));
            assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)-1));
            assertEquals((byte)-5, MathUtils.copySign((byte)-5, (byte)1));
            assertEquals((byte)-5, MathUtils.copySign((byte)-5, (byte)-1));
            assertEquals((byte)-128, MathUtils.copySign((byte)-128, (byte)1));
            
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignShort() {
        try {
            assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
            assertEquals((short)5, MathUtils.copySign((short)5, (short)-1));
            assertEquals((short)-5, MathUtils.copySign((short)-5, (short)1));
            assertEquals((short)-5, MathUtils.copySign((short)-5, (short)-1));
            assertEquals((short)-32768, MathUtils.copySign((short)-32768, (short)1));
            
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignInt() {
        try {
            assertEquals(5, MathUtils.copySign(5, 1));
            assertEquals(5, MathUtils.copySign(5, -1));
            assertEquals(-5, MathUtils.copySign(-5, 1));
            assertEquals(-5, MathUtils.copySign(-5, -1));
            assertEquals(Integer.MIN_VALUE, MathUtils.copySign(Integer.MIN_VALUE, 1));
            
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignLong() {
        try {
            assertEquals(5L, MathUtils.copySign(5L, 1L));
            assertEquals(5L, MathUtils.copySign(5L, -1L));
            assertEquals(-5L, MathUtils.copySign(-5L, 1L));
            assertEquals(-5L, MathUtils.copySign(-5L, -1L));
            assertEquals(Long.MIN_VALUE, MathUtils.copySign(Long.MIN_VALUE, 1L));
            
            fail("Expected MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCheckFinite() {
        try {
            MathUtils.checkFinite(0.0);
            MathUtils.checkFinite(1.0);
            MathUtils.checkFinite(-1.0);
            
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testCheckFiniteArray() {
        try {
            MathUtils.checkFinite(new double[] {0.0, 1.0, 2.0, 3.0, 4.0});
            MathUtils.checkFinite(new double[] {-1.0, -2.0, -3.0, -4.0});
            MathUtils.checkFinite(new double[] {0.0, 1.0, Double.NaN, 3.0, 4.0});
            
            fail("Expected NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            // expected behavior
        }
    }
    
    @Test
    public void testCheckNotNullPattern() {
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 5);
            
            fail("Expected NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
        }
    }
    
    @Test
    public void testCheckNotNull() {
        try {
            MathUtils.checkNotNull(null);
            
            fail("Expected NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            // expected behavior
        }
    }
}