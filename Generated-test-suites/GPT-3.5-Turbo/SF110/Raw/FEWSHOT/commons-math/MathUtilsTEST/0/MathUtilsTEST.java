package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.*;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {
    @Test
    public void testHash() {
        assertEquals(Double.valueOf(4.0).hashCode(), MathUtils.hash(4.0));
        assertEquals(Double.valueOf(2.5).hashCode(), MathUtils.hash(2.5));
    }

    @Test
    public void testHashArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        assertEquals(Arrays.hashCode(array1), MathUtils.hash(array1));
        assertEquals(Arrays.hashCode(array2), MathUtils.hash(array2));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(MathUtils.TWO_PI, 0), 0.0001);
        assertEquals(Math.PI, MathUtils.normalizeAngle(MathUtils.TWO_PI + Math.PI, Math.PI), 0.0001);
        assertEquals(-Math.PI, MathUtils.normalizeAngle(-Math.PI, Math.PI), 0.0001);
        assertEquals(0.5236, MathUtils.normalizeAngle(3.14, 0), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(7.0, 6.0, 0), 0.0001);
        assertEquals(0.5236, MathUtils.reduce(7.0, 6.0, 0.5236), 0.0001);
        assertEquals(-0.5236, MathUtils.reduce(-2.0, 6.0, -3.0), 0.0001);
        assertEquals(2.0, MathUtils.reduce(-3.0, -6.0, 4.0), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -3;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
        magnitude = -5;
        sign = 3;
        assertEquals(5, MathUtils.copySign(magnitude, sign));
        magnitude = -128;
        sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 500;
        short sign = -300;
        assertEquals(-500, MathUtils.copySign(magnitude, sign));
        magnitude = -500;
        sign = 300;
        assertEquals(500, MathUtils.copySign(magnitude, sign));
        magnitude = -32768;
        sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 500;
        int sign = -300;
        assertEquals(-500, MathUtils.copySign(magnitude, sign));
        magnitude = -500;
        sign = 300;
        assertEquals(500, MathUtils.copySign(magnitude, sign));
        magnitude = Integer.MIN_VALUE;
        sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 500L;
        long sign = -300L;
        assertEquals(-500L, MathUtils.copySign(magnitude, sign));
        magnitude = -500L;
        sign = 300L;
        assertEquals(500L, MathUtils.copySign(magnitude, sign));
        magnitude = Long.MIN_VALUE;
        sign = -1L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite() {
        double finiteNumber = 12.5;
        MathUtils.checkFinite(finiteNumber);
        double infiniteNumber = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(infiniteNumber);
            fail();
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
        }
        double nanNumber = Double.NaN;
        try {
            MathUtils.checkFinite(nanNumber);
            fail();
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isNaN(e.getArgument()));
        }
    }

    @Test
    public void testCheckFiniteArray() {
        double[] finiteArray = {1.0, 2.5, 3.0};
        MathUtils.checkFinite(finiteArray);
        double[] infiniteArray = {1.0, 2.0, Double.POSITIVE_INFINITY};
        try {
            MathUtils.checkFinite(infiniteArray);
            fail();
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isInfinite(e.getArgument()));
            assertEquals(2, e.getIndex());
        }
        double[] nanArray = {Double.NaN, 1.0, 2.0};
        try {
            MathUtils.checkFinite(nanArray);
            fail();
        } catch (NotFiniteNumberException e) {
            assertTrue(Double.isNaN(e.getArgument()));
            assertEquals(0, e.getIndex());
        }
    }

    @Test
    public void testCheckNotNull() {
        Object notNullObject = new Object();
        try {
            MathUtils.checkNotNull(notNullObject);
        } catch (NullArgumentException e) {
            fail();
        }
        Object nullObject = null;
        try {
            MathUtils.checkNotNull(nullObject);
            fail();
        } catch (NullArgumentException e) {
            assertNull(e.getPattern());
        }
    }
}