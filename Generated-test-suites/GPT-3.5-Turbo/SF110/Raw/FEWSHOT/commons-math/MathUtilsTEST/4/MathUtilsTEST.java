
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 0.0;
        int expected = Double.valueOf(value).hashCode();
        assertEquals(expected, MathUtils.hash(value), 0);
        
        value = 1.234;
        expected = Double.valueOf(value).hashCode();
        assertEquals(expected, MathUtils.hash(value), 0);
        
        value = -5.678;
        expected = Double.valueOf(value).hashCode();
        assertEquals(expected, MathUtils.hash(value), 0);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = null;
        int expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value), 0);
        
        value = new double[]{};
        expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value), 0);
        
        value = new double[]{1.0};
        expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value), 0);
        
        value = new double[]{1.0, 2.0, 3.0};
        expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value), 0);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 0.0;
        double center = 0.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0);
        
        a = 1.234;
        center = 0.0;
        expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0);
        
        a = 5.678;
        center = 0.0;
        expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0);
    }
    
    @Test
    public void testReduce() {
        double a = 0.0;
        double period = 1.0;
        double offset = 0.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0);
        
        a = 1.234;
        period = 3.456;
        offset = 2.0;
        p = FastMath.abs(period);
        expected = a - p * FastMath.floor((a - offset) / p) - offset;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0);
        
        a = 5.678;
        period = 9.876;
        offset = 4.321;
        p = FastMath.abs(period);
        expected = a - p * FastMath.floor((a - offset) / p) - offset;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 0;
        byte sign = 1;
        byte expected = (byte) -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -5;
        sign = 1;
        expected = (byte) -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -128;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            assertTrue(false);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 0;
        short sign = 1;
        short expected = (short) -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -12345;
        sign = 1;
        expected = (short) -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -32768;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            assertTrue(false);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 0;
        int sign = 1;
        int expected = -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -123456;
        sign = 1;
        expected = -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = Integer.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            assertTrue(false);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 0L;
        long sign = 1L;
        long expected = -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = -1234567890123L;
        sign = 1L;
        expected = -magnitude;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
        
        magnitude = Long.MIN_VALUE;
        sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            assertTrue(false);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 0.0;
        MathUtils.checkFinite(x);
        
        x = 1.234;
        MathUtils.checkFinite(x);
        
        x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
        }
        
        x = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
        }
        
        x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = null;
        MathUtils.checkFinite(val);
        
        val = new double[]{};
        MathUtils.checkFinite(val);
        
        val = new double[]{0.0};
        MathUtils.checkFinite(val);
        
        val = new double[]{1.234, -5.678, Double.POSITIVE_INFINITY};
        try {
            MathUtils.checkFinite(val);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecificPattern());
            assertEquals(2, e.getArguments().length);
            assertEquals(2, e.getArguments()[0]);
        }
        
        val = new double[]{Double.NEGATIVE_INFINITY, 1.234, 2.345};
        try {
            MathUtils.checkFinite(val);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecificPattern());
            assertEquals(0, e.getArguments().length);
        }
        
        val = new double[]{1.234, 2.345, Double.NaN};
        try {
            MathUtils.checkFinite(val);
            assertTrue(false);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getSpecifier());
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecificPattern());
            assertEquals(2, e.getArguments().length);
            assertEquals(2, e.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull_objectLocalizableArgs() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.FUNCTION_NOT_DIFFERENTIABLE_AT_POINTS;
        Object[] args = new Object[]{"x = 0"};
        
        MathUtils.checkNotNull(o, pattern, args);
        
        o = null;
        try {
            MathUtils.checkNotNull(o, pattern, args);
            assertTrue(false);
        } catch (NullArgumentException e) {
            assertNotNull(e.getSpecifier());
            assertEquals(pattern, e.getSpecifier());
            assertEquals(args.length, e.getArguments().length);
            assertEquals(args[0], e.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull_object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        
        o = null;
        try {
            MathUtils.checkNotNull(o);
            assertTrue(false);
        } catch (NullArgumentException e) {
            assertNotNull(e.getSpecifier());
            assertEquals(null, e.getSpecifier());
            assertEquals(0, e.getArguments().length);
        }
    }
}

