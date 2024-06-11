package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(Double.valueOf(0).hashCode(), MathUtils.hash(0.0));
        assertEquals(Double.valueOf(1).hashCode(), MathUtils.hash(1.0));
        assertEquals(Double.valueOf(-1).hashCode(), MathUtils.hash(-1.0));
        assertEquals(Double.valueOf(Double.MAX_VALUE).hashCode(), MathUtils.hash(Double.MAX_VALUE));
        assertEquals(Double.valueOf(Double.MIN_VALUE).hashCode(), MathUtils.hash(Double.MIN_VALUE));
        assertEquals(Double.valueOf(Double.POSITIVE_INFINITY).hashCode(), MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(Double.valueOf(Double.NEGATIVE_INFINITY).hashCode(), MathUtils.hash(Double.NEGATIVE_INFINITY));
        assertEquals(Double.valueOf(Double.NaN).hashCode(), MathUtils.hash(Double.NaN));
    }

    @Test
    public void testHashDoubleArray() {
        // Test with empty array
        double[] emptyArray = {};
        assertEquals(Arrays.hashCode(emptyArray), MathUtils.hash(emptyArray));
        
        // Test with non-empty array
        double[] array = {0, 1, 2, 3};
        assertEquals(Arrays.hashCode(array), MathUtils.hash(array));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(-6.283185307179586, 0.0), 0.0);
        assertEquals(1.0, MathUtils.normalizeAngle(7.283185307179586, 0.0), 0.0);
        assertEquals(-1.0, MathUtils.normalizeAngle(-7.283185307179586, 0.0), 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.0), 0.0);
        assertEquals(1.0, MathUtils.reduce(1.0, 1.0, 0.0), 0.0);
        assertEquals(0.5, MathUtils.reduce(1.5, 1.0, 0.0), 0.0);
        assertEquals(-0.5, MathUtils.reduce(-0.5, 1.0, 0.0), 0.0);
        assertEquals(0.1, MathUtils.reduce(0.1, 0.2, 0.0), 0.0);
        assertEquals(0.0, MathUtils.reduce(0.1, 0.2, 0.1), 0.0);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign((byte) 0, (byte) 1));
        assertEquals(0, MathUtils.copySign((byte) 0, (byte) -1));
        assertEquals(1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals(-1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals(1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals(-1, MathUtils.copySign((byte) -1, (byte) -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        MathUtils.copySign((byte) 128, (byte) -1);
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign((short) 0, (short) 1));
        assertEquals(0, MathUtils.copySign((short) 0, (short) -1));
        assertEquals(1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals(-1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals(1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals(-1, MathUtils.copySign((short) -1, (short) -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        MathUtils.copySign((short) 32768, (short) -1);
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(0, MathUtils.copySign(0, 1));
        assertEquals(0, MathUtils.copySign(0, -1));
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(-1, -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        MathUtils.copySign(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(0L, MathUtils.copySign(0L, 1L));
        assertEquals(0L, MathUtils.copySign(0L, -1L));
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        MathUtils.copySign(Long.MIN_VALUE, -1L);
    }

    @Test
    public void testCheckFiniteDouble() throws NotFiniteNumberException {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(Double.MAX_VALUE);
        MathUtils.checkFinite(Double.MIN_VALUE);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);

        boolean exceptionThrown = false;
        try {
            MathUtils.checkFinite(Double.NaN);
        } catch (NotFiniteNumberException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCheckFiniteDoubleArray() throws NotFiniteNumberException {
        // Test with empty array
        double[] emptyArray = {};
        MathUtils.checkFinite(emptyArray);

        // Test with non-empty array
        double[] array = {0.0, 1.0, -1.0, Double.MAX_VALUE, Double.MIN_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        MathUtils.checkFinite(array);

        boolean exceptionThrown = false;
        double[] arrayWithNaN = {Double.NaN};
        try {
            MathUtils.checkFinite(arrayWithNaN);
        } catch (NotFiniteNumberException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object object = new Object();
        MathUtils.checkNotNull(object);

        boolean exceptionThrown = false;
        try {
            MathUtils.checkNotNull(null);
        } catch (NullArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        Object object = new Object();
        MathUtils.checkNotNull(object, LocalizedFormats.ARRAY_ELEMENT, 0);

        boolean exceptionThrown = false;
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
        } catch (NullArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}