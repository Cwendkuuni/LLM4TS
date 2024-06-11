package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash_double() {
        assertEquals(0, MathUtils.hash(0));
        assertEquals(-1729459103, MathUtils.hash(-123.456));
        assertEquals(1818496485, MathUtils.hash(123.456));
    }

    @Test
    public void testHash_doubleArray() {
        assertEquals(0, MathUtils.hash(new double[] {}));
        assertEquals(1581115368, MathUtils.hash(new double[] {1.23, 4.56, 7.89}));
        assertNotEquals(1581115368, MathUtils.hash(new double[] {1.23, 4.56, 7.89, 0}));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(-0.14159265358979312, MathUtils.normalizeAngle(6.142857142857143, 0), 1e-12);
        assertEquals(2.384748219072765e-8, MathUtils.normalizeAngle(6.2831852801354925, 0), 1e-12);
        assertEquals(3.9269908169872414, MathUtils.normalizeAngle(-3.141592653589793, 0), 1e-12);
        assertEquals(-2.751919970347491E-9, MathUtils.normalizeAngle(-6.283185304410812, 0), 1e-12);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(10, 10, 0), 1e-12);
        assertEquals(8.12, MathUtils.reduce(18.12, 10, 0), 1e-12);
        assertEquals(2.22, MathUtils.reduce(12.22, 10, 0), 1e-12);
        assertEquals(9.88, MathUtils.reduce(-0.12, 10, 0), 1e-12);
    }

    @Test
    public void testCopySign_byte() throws MathArithmeticException {
        assertEquals((-1), MathUtils.copySign((byte)1, (byte)-1));
        assertEquals(1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals(0, MathUtils.copySign((byte)0, (byte)0));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_Exception() throws MathArithmeticException {
        MathUtils.copySign(Byte.MIN_VALUE, (byte)1);
    }

    @Test
    public void testCopySign_short() throws MathArithmeticException {
        assertEquals((-1), MathUtils.copySign((short)1, (short)-1));
        assertEquals(1, MathUtils.copySign((short)-1, (short)1));
        assertEquals(0, MathUtils.copySign((short)0, (short)0));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_Exception() throws MathArithmeticException {
        MathUtils.copySign(Short.MIN_VALUE, (short)1);
    }

    @Test
    public void testCopySign_int() throws MathArithmeticException {
        assertEquals((-1), MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(-1, 1));
        assertEquals(0, MathUtils.copySign(0, 0));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_Exception() throws MathArithmeticException {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals((-1L), MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(-1L, 1L));
        assertEquals(0L, MathUtils.copySign(0L, 0L));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_Exception() throws MathArithmeticException {
        MathUtils.copySign(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testCheckFinite_double() throws NotFiniteNumberException {
        MathUtils.checkFinite(0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_Exception() throws NotFiniteNumberException {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFinite_doubleArray() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[] {1.23, 4.56, 7.89});
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_Exception() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[] {1.23, Double.NaN, 7.89});
    }

    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() throws NullArgumentException {
        MathUtils.checkNotNull("", LocalizedFormats.ARRAY_ELEMENT, 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Localizable_ObjectArray_Exception() throws NullArgumentException {
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
    }

    @Test
    public void testCheckNotNull_Object() throws NullArgumentException {
        MathUtils.checkNotNull("");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Exception() throws NullArgumentException {
        MathUtils.checkNotNull(null);
    }
}