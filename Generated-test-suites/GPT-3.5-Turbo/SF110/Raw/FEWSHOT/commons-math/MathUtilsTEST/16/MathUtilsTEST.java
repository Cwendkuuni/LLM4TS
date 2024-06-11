package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHashWithDoubleValue() {
        assertEquals(1234567, MathUtils.hash(1234567.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-1234567, MathUtils.hash(-1234567.0));
        assertEquals(2147483647, MathUtils.hash(Double.MAX_VALUE));
        assertEquals(-2147483648, MathUtils.hash(Double.MIN_VALUE));
    }

    @Test
    public void testHashWithDoubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {3.0, 2.0, 1.0};

        assertEquals(array1.hashCode(), MathUtils.hash(array1));
        assertEquals(array2.hashCode(), MathUtils.hash(array2));
        assertEquals(array3.hashCode(), MathUtils.hash(array3));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0001);
        assertEquals(1.0, MathUtils.normalizeAngle(1.0, 0.0), 0.0001);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.0001);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(-1.5707963267948966, 0.0), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 0.0, 0.0), 0.0001);
        assertEquals(1.0, MathUtils.reduce(1.0, 0.0, 0.0), 0.0001);
        assertEquals(2.0, MathUtils.reduce(2.0, 1.0, 0.0), 0.0001);
        assertEquals(-2.0, MathUtils.reduce(-2.0, 1.0, 0.0), 0.0001);
        assertEquals(0.0, MathUtils.reduce(-2.0, 2.0, 2.0), 0.0001);
    }

    @Test
    public void testCopySignWithByteMagnitudeAndSign() {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)1));
        assertEquals((byte)5, MathUtils.copySign((byte)-5, (byte)-1));
        assertEquals((byte)(-5), MathUtils.copySign((byte)5, (byte)-1));
        assertEquals((byte)(-5), MathUtils.copySign((byte)-5, (byte)1));

        try {
            MathUtils.copySign((byte)-128, (byte)1);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignWithShortMagnitudeAndSign() {
        assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
        assertEquals((short)5, MathUtils.copySign((short)-5, (short)-1));
        assertEquals((short)(-5), MathUtils.copySign((short)5, (short)-1));
        assertEquals((short)(-5), MathUtils.copySign((short)-5, (short)1));

        try {
            MathUtils.copySign((short)-32768, (short)1);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignWithIntMagnitudeAndSign() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(5, MathUtils.copySign(-5, -1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(-5, MathUtils.copySign(-5, 1));

        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignWithLongMagnitudeAndSign() {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(5L, MathUtils.copySign(-5L, -1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
        assertEquals(-5L, MathUtils.copySign(-5L, 1L));

        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFiniteWithDouble() {
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
        }

        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
        }

        try {
            MathUtils.checkFinite(Double.NaN);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isNaN(e.getArgument()));
        }

        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(123.456);
    }

    @Test
    public void testCheckFiniteWithDoubleArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, Double.POSITIVE_INFINITY, 3.0};
        double[] array3 = {1.0, Double.NEGATIVE_INFINITY, 3.0};
        double[] array4 = {1.0, Double.NaN, 3.0};

        try {
            MathUtils.checkFinite(array2);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
            assertEquals(1, e.getIndex());
        }

        try {
            MathUtils.checkFinite(array3);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
            assertEquals(1, e.getIndex());
        }

        try {
            MathUtils.checkFinite(array4);
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isNaN(e.getArgument()));
            assertEquals(1, e.getIndex());
        }

        MathUtils.checkFinite(array1);
    }

    @Test
    public void testCheckNotNull() {
        Object obj = new Object();

        try {
            MathUtils.checkNotNull(null);
        } catch (NullArgumentException e) {
            assertTrue(true);
        }

        try {
            MathUtils.checkNotNull(null, LocalizedFormats.PATTERN, "arg");
        } catch (NullArgumentException e) {
            assertTrue(true);
            assertEquals("arg", e.getArguments()[0]);
        }

        MathUtils.checkNotNull(obj);
        MathUtils.checkNotNull(obj, LocalizedFormats.PATTERN, "arg");
    }
}