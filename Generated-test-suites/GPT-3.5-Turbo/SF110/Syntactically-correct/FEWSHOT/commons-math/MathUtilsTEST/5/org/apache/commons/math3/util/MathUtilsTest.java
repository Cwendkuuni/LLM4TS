package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash() {
        assertEquals(1234, MathUtils.hash(1234.0));
        assertEquals(-5678, MathUtils.hash(-5678.0));
        assertEquals(0, MathUtils.hash(0.0));
    }

    @Test
    public void testHashArray() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {1.0, 2.0, 3.0};
        double[] arr3 = {4.0, 5.0, 6.0};

        assertEquals(arr1.hashCode(), MathUtils.hash(arr1));
        assertEquals(arr2.hashCode(), MathUtils.hash(arr2));
        assertNotEquals(arr1.hashCode(), MathUtils.hash(arr3));
        assertNotEquals(arr2.hashCode(), MathUtils.hash(arr3));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(2.0, MathUtils.normalizeAngle(8.0, -3.0), 0.01);
        assertEquals(3.0, MathUtils.normalizeAngle(9.0, -3.0), 0.01);
        assertEquals(4.0, MathUtils.normalizeAngle(10.0, -3.0), 0.01);
    }

    @Test
    public void testReduce() {
        assertEquals(4.0, MathUtils.reduce(10.0, 3.0, 1.0), 0.01);
        assertEquals(6.0, MathUtils.reduce(16.0, -5.0, -2.0), 0.01);
        assertEquals(2.0, MathUtils.reduce(-2.0, 3.0, 1.0), 0.01);
    }

    @Test
    public void testCopySignByte() {
        assertEquals(30, MathUtils.copySign((byte)30, (byte)1));
        assertEquals(-30, MathUtils.copySign((byte)30, (byte)-1));
        assertEquals(-127, MathUtils.copySign((byte)127, (byte)-1));
        assertEquals(0, MathUtils.copySign((byte)0, (byte)0));
    }

    @Test
    public void testCopySignShort() {
        assertEquals(30000, MathUtils.copySign((short)30000, (short)1));
        assertEquals(-30000, MathUtils.copySign((short)30000, (short)-1));
        assertEquals(-32767, MathUtils.copySign((short)32767, (short)-1));
        assertEquals(0, MathUtils.copySign((short)0, (short)0));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(300000000, MathUtils.copySign(300000000, 1));
        assertEquals(-300000000, MathUtils.copySign(300000000, -1));
        assertEquals(-2147483647, MathUtils.copySign(2147483647, -1));
        assertEquals(0, MathUtils.copySign(0, 0));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(3000000000000000000L, MathUtils.copySign(3000000000000000000L, 1L));
        assertEquals(-3000000000000000000L, MathUtils.copySign(3000000000000000000L, -1L));
        assertEquals(-9223372036854775807L, MathUtils.copySign(9223372036854775807L, -1L));
        assertEquals(0, MathUtils.copySign(0L, 0L));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteException() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {Double.POSITIVE_INFINITY, 2.0, 3.0};

        MathUtils.checkFinite(arr1);
        NotFiniteNumberException exception = assertThrows(NotFiniteNumberException.class, () -> {
            MathUtils.checkFinite(arr2);
        });

        assertEquals(LocalizedFormats.ARRAY_ELEMENT, exception.getPattern());
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullException() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullWithPattern() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.INVALID_PARAMETERS);
        NullArgumentException exception = assertThrows(NullArgumentException.class, () -> {
            MathUtils.checkNotNull(null, LocalizedFormats.INVALID_PARAMETERS);
        });

        assertEquals(LocalizedFormats.INVALID_PARAMETERS, exception.getPattern());
    }
}