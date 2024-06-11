
package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.0;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {10.0, 20.0, 30.0};
        assertEquals(Arrays.hashCode(value), MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double result = MathUtils.normalizeAngle(7.0, 0.0);
        assertTrue(result >= 0 && result < MathUtils.TWO_PI);
    }

    @Test
    public void testReduce() {
        double result = MathUtils.reduce(10.0, 5.0, 0.0);
        assertTrue(result >= 0 && result < 5.0);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 1000;
        short sign = -500;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 10000;
        int sign = -5000;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 100000;
        long sign = -50000;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_Exception() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY};
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(val));
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Exception() {
        MathUtils.checkNotNull(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_LocalizableObjectArray_Exception() {
        MathUtils.checkNotNull(null, LocalizedFormats.NUMBER_TOO_SMALL, 0);
    }
}

