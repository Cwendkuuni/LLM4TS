package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(-449263482, MathUtils.hash(-3.141592653589793));
        assertEquals(107000597, MathUtils.hash(2.718281828459045));
        assertEquals(-1441281091, MathUtils.hash(0.0));
        assertEquals(0, MathUtils.hash(Double.NaN));
        assertEquals(2147483647, MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(-2147483648, MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHash_doubleArray() {
        double[] array = {1.0, 2.0, 3.0};
        assertEquals(-2015904632, MathUtils.hash(array));
        
        double[] emptyArray = {};
        assertEquals(1, MathUtils.hash(emptyArray));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(7.855550760543978, 3.141592653589793), 0.00001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-9.425190727459342, -3.141592653589793), 0.00001);
        assertEquals(-0.6390982703873142, MathUtils.normalizeAngle(4.545697607480997, -7.853981633974483), 0.00001);
        assertEquals(7.853981633974483, MathUtils.normalizeAngle(-1.5707963267948966, -3.141592653589793), 0.00001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.5707963267948966, MathUtils.reduce(7.855550760543978, 3.141592653589793, 3.141592653589793), 0.00001);
        assertEquals(-2.221441469079183, MathUtils.reduce(-9.425190727459342, 3.141592653589793, -3.141592653589793), 0.00001);
        assertEquals(-3.1271389035204544, MathUtils.reduce(4.545697607480997, -7.853981633974483, -7.853981633974483), 0.00001);
        assertEquals(3.141592653589793, MathUtils.reduce(-1.5707963267948966, -3.141592653589793, -3.141592653589793), 0.00001);
    }

    @Test
    public void testCopySign_byte() {
        assertEquals((byte)3, MathUtils.copySign((byte)3, (byte)1));
        assertEquals((byte)3, MathUtils.copySign((byte)3, (byte)-1));
        assertEquals((byte)-3, MathUtils.copySign((byte)-3, (byte)1));
        try {
            MathUtils.copySign((byte)-128, (byte)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Passed
        }
    }

    @Test
    public void testCopySign_short() {
        assertEquals((short)3, MathUtils.copySign((short)3, (short)1));
        assertEquals((short)3, MathUtils.copySign((short)3, (short)-1));
        assertEquals((short)-3, MathUtils.copySign((short)-3, (short)1));
        try {
            MathUtils.copySign((short)-32768, (short)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Passed
        }
    }

    @Test
    public void testCopySign_int() {
        assertEquals(3, MathUtils.copySign(3, 1));
        assertEquals(3, MathUtils.copySign(3, -1));
        assertEquals(-3, MathUtils.copySign(-3, 1));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Passed
        }
    }

    @Test
    public void testCopySign_long() {
        assertEquals(3L, MathUtils.copySign(3L, 1L));
        assertEquals(3L, MathUtils.copySign(3L, -1L));
        assertEquals(-3L, MathUtils.copySign(-3L, 1L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Passed
        }
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(0.0);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Passed
        }
        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Passed
        }
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Passed
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] array = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(array);
        
        double[] arrayWithInfinity = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(arrayWithInfinity);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Passed
        }
        
        double[] arrayWithNaN = {1.0, 2.0, Double.NaN};
        try {
            MathUtils.checkFinite(arrayWithNaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Passed
        }
        
        double[] emptyArray = {};
        MathUtils.checkFinite(emptyArray);
    }

    @Test
    public void testCheckNotNull_object_Localizable_ObjectArray() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_ELEMENT, new Object[] {0});
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, new Object[] {0});
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Passed
        }
    }

    @Test
    public void testCheckNotNull_object() {
        MathUtils.checkNotNull(new Object());
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Passed
        }
    }

}