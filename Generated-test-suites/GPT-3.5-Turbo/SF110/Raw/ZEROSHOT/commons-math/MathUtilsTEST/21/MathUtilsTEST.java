package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(1, MathUtils.hash(1.0));
        assertEquals(-1, MathUtils.hash(-1.0));
    }

    @Test
    public void testHashArray() {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {1.0, 2.0, 3.0};
        double[] c = {3.0, 2.0, 1.0};

        assertEquals(MathUtils.hash(a), MathUtils.hash(b));
        assertNotEquals(MathUtils.hash(a), MathUtils.hash(c));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(9.42477796076938, 0.0), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793, 3.141592653589793), 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 2 * MathUtils.TWO_PI, 0.0), 0.0);
        assertEquals(2.0, MathUtils.reduce(8.0, 3.0, 1.0), 0.0);
        assertEquals(4.0, MathUtils.reduce(-2.0, 4.0, -2.0), 0.0);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        assertEquals(2, MathUtils.copySign((byte) 2, (byte) 3));
        assertEquals(-2, MathUtils.copySign((byte) -2, (byte) 3));
        assertEquals(0, MathUtils.copySign((byte) 0, (byte) 3));
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        assertEquals(2, MathUtils.copySign((short) 2, (short) 3));
        assertEquals(-2, MathUtils.copySign((short) -2, (short) 3));
        assertEquals(0, MathUtils.copySign((short) 0, (short) 3));
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        assertEquals(2, MathUtils.copySign(2, 3));
        assertEquals(-2, MathUtils.copySign(-2, 3));
        assertEquals(0, MathUtils.copySign(0, 3));
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        assertEquals(2, MathUtils.copySign(2L, 3L));
        assertEquals(-2, MathUtils.copySign(-2L, 3L));
        assertEquals(0, MathUtils.copySign(0L, 3L));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPattern() throws NullArgumentException {
        MathUtils.checkNotNull(null, LocalizedFormats.NOT_SUPPORTED);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull() throws NullArgumentException {
        MathUtils.checkNotNull(null);
    }
}