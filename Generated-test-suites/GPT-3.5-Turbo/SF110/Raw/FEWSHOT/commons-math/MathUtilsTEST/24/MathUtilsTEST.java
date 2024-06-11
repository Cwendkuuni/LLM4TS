package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash() {
        assertEquals(-430150611, MathUtils.hash(123.456));
        assertEquals(-164387456, MathUtils.hash(-789.012));
        assertEquals(-569222021, MathUtils.hash(0.0));
    }

    @Test
    public void testHashArray() {
        assertEquals(949811795, MathUtils.hash(new double[]{1.23, 4.56, 7.89}));
        assertEquals(-1971838144, MathUtils.hash(new double[]{-1.23, -4.56, -7.89}));
        assertEquals(0, MathUtils.hash(new double[]{}));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(2.0, MathUtils.normalizeAngle(8.0, 0.0), 0.000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.000001);
        assertEquals(0.5235987755982988, MathUtils.normalizeAngle(7.523598775598299, 0.0), 0.000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-4.61238898038469, 0.0), 0.000001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.1, MathUtils.reduce(6.3, 6.2, 0.0), 0.000001);
        assertEquals(0.0, MathUtils.reduce(12.4, 6.2, 0.0), 0.000001);
        assertEquals(-2.2, MathUtils.reduce(-18.3, 6.1, 0.1), 0.000001);
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.0), 0.000001);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals((byte) 10, MathUtils.copySign((byte) 10, (byte) 1));
        assertEquals((byte) -10, MathUtils.copySign((byte) -10, (byte) 1));
        assertEquals((byte) 127, MathUtils.copySign((byte) -127, (byte) -1));
        assertEquals((byte) 0, MathUtils.copySign((byte) 0, (byte) 0));

        try {
            MathUtils.copySign((byte) -128, (byte) 1);
        } catch (MathArithmeticException e) {
            assertNotNull(e);
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals((short) 100, MathUtils.copySign((short) 100, (short) 1));
        assertEquals((short) -100, MathUtils.copySign((short) -100, (short) 1));
        assertEquals((short) 32767, MathUtils.copySign((short) -32767, (short) -1));
        assertEquals((short) 0, MathUtils.copySign((short) 0, (short) 0));

        try {
            MathUtils.copySign((short) -32768, (short) 1);
        } catch (MathArithmeticException e) {
            assertNotNull(e);
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(1000, MathUtils.copySign(1000, 1));
        assertEquals(-1000, MathUtils.copySign(-1000, 1));
        assertEquals(Integer.MAX_VALUE, MathUtils.copySign(Integer.MAX_VALUE, -1));
        assertEquals(0, MathUtils.copySign(0, 0));

        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
        } catch (MathArithmeticException e) {
            assertNotNull(e);
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(10000000000L, MathUtils.copySign(10000000000L, 1L));
        assertEquals(-10000000000L, MathUtils.copySign(-10000000000L, 1L));
        assertEquals(Long.MAX_VALUE, MathUtils.copySign(Long.MAX_VALUE, -1L));
        assertEquals(0L, MathUtils.copySign(0L, 0L));

        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
        } catch (MathArithmeticException e) {
            assertNotNull(e);
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(123.456);
        MathUtils.checkFinite(-789.012);
        MathUtils.checkFinite(0.0);

        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }

        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }

        try {
            MathUtils.checkFinite(Double.NaN);
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        MathUtils.checkFinite(new double[]{1.23, 4.56, 7.89});
        MathUtils.checkFinite(new double[]{-1.23, -4.56, -7.89});
        MathUtils.checkFinite(new double[]{});

        try {
            MathUtils.checkFinite(new double[]{Double.POSITIVE_INFINITY, 1.23});
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }

        try {
            MathUtils.checkFinite(new double[]{Double.NEGATIVE_INFINITY, -1.23});
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }

        try {
            MathUtils.checkFinite(new double[]{Double.NaN, 0.0});
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckNotNullPattern() throws NullArgumentException {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_OF_1, "array", 100, 101);
        MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_OF_1);
        MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_OF_1, "array", 100, 101, 102);

        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_OF_1, "array", 100, 101);
        } catch (NullArgumentException e) {
            assertNotNull(e);
            assertEquals(LocalizedFormats.ARRAY_SIZES_SHOULD_HAVE_DIFFERENCE_OF_1, e.getLocalizedMessage());
        }
    }

    @Test
    public void testCheckNotNull() throws NullArgumentException {
        MathUtils.checkNotNull(new Object());
        MathUtils.checkNotNull("");

        try {
            MathUtils.checkNotNull(null);
        } catch (NullArgumentException e) {
            assertNotNull(e);
        }
    }
}