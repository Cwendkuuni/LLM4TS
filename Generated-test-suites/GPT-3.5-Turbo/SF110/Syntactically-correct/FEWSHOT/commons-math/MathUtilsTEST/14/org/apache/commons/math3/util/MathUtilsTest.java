package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.util.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(Double.valueOf(0).hashCode(), MathUtils.hash(0.0));
        assertEquals(Double.valueOf(1.234).hashCode(), MathUtils.hash(1.234));
        assertEquals(Double.valueOf(-1.234).hashCode(), MathUtils.hash(-1.234));
    }

    @Test
    public void testHash_doubleArray() {
        double[] array1 = {0.0, 1.234, -1.234};
        double[] array2 = {0.0, 1.234, -1.234};
        double[] array3 = {1.234, -1.234, 0.0};

        assertEquals(Arrays.hashCode(array1), MathUtils.hash(array1));
        assertEquals(Arrays.hashCode(array2), MathUtils.hash(array2));
        assertEquals(Arrays.hashCode(array3), MathUtils.hash(array3));
    }

    @Test
    public void testNormalizeAngle() {
        double center = 0.0;

        assertEquals(0.0, MathUtils.normalizeAngle(0.0, center), 0.0);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(3.141592653589793, center), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, center), 0.0);
        assertEquals(0.1, MathUtils.normalizeAngle(6.383185307179586, center), 0.0);
        assertEquals(-0.1, MathUtils.normalizeAngle(-5.983185307179586, center), 0.0);
        assertEquals(0.1, MathUtils.normalizeAngle(12.566370614359172, center), 0.0);
    }

    @Test
    public void testReduce() {
        double period = 4.0;
        double offset = 1.0;

        assertEquals(-1.0, MathUtils.reduce(0.0, period, offset), 0.0);
        assertEquals(3.0, MathUtils.reduce(4.0, period, offset), 0.0);
        assertEquals(2.0, MathUtils.reduce(9.0, period, offset), 0.0);
        assertEquals(3.5, MathUtils.reduce(15.5, period, offset), 0.0);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 0;
        byte sign = 1;

        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -1;
        sign = -1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = 127;
        sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -128;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 0;
        short sign = 1;

        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -1;
        sign = -1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = 1;
        sign = 0;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = 32767;
        sign = -1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -32768;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 0;
        int sign = 1;

        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -1;
        sign = -1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = 1;
        sign = 0;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MAX_VALUE;
        sign = -1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 0L;
        long sign = 1L;

        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -1L;
        sign = -1L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = 1L;
        sign = 0L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Long.MAX_VALUE;
        sign = -1L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Long.MIN_VALUE;
        sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double finiteNum = 1.234;
        MathUtils.checkFinite(finiteNum); // No exception expected

        double infiniteNum = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(infiniteNum);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getMessage());
        }

        double nanNum = Double.NaN;
        try {
            MathUtils.checkFinite(nanNum);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] finiteArray = {1.234, 2.345, 3.456};
        MathUtils.checkFinite(finiteArray); // No exception expected

        double[] infiniteArray = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1.0};
        try {
            MathUtils.checkFinite(infiniteArray);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getMessage());
        }

        double[] nanArray = {1.0, Double.NaN, 2.0};
        try {
            MathUtils.checkFinite(nanArray);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull_object_Localizable_ObjectArray() {
        Object nonNullObject = new Object();
        MathUtils.checkNotNull(nonNullObject, LocalizedFormats.abc, 123); // No exception expected

        Object nullObject = null;
        try {
            MathUtils.checkNotNull(nullObject, LocalizedFormats.def, 456);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull_object() {
        Object nonNullObject = new Object();
        MathUtils.checkNotNull(nonNullObject); // No exception expected

        Object nullObject = null;
        try {
            MathUtils.checkNotNull(nullObject);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }
}