
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(12345, MathUtils.hash(12345.0));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-98765, MathUtils.hash(-98765.0));
    }

    @Test
    public void testHash_doubleArray() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {1.0, 2.0, 3.0};
        double[] arr3 = {1.0, 2.0, 4.0};

        assertEquals(Arrays.hashCode(arr1), MathUtils.hash(arr1));
        assertEquals(Arrays.hashCode(arr2), MathUtils.hash(arr2));
        assertNotEquals(Arrays.hashCode(arr3), MathUtils.hash(arr2));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.000001);
        assertEquals(1.0, MathUtils.normalizeAngle(MathUtils.TWO_PI + 1.0, 0.0), 0.000001);
        assertEquals(-1.0, MathUtils.normalizeAngle(-MathUtils.TWO_PI - 1.0, 0.0), 0.000001);
        assertEquals(3.0, MathUtils.normalizeAngle(MathUtils.TWO_PI + 3.0, MathUtils.TWO_PI), 0.000001);
        assertEquals(-5.0, MathUtils.normalizeAngle(-MathUtils.TWO_PI - 5.0, MathUtils.TWO_PI), 0.000001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 0.0, 0.0), 0.000001);
        assertEquals(1.0, MathUtils.reduce(1.0, 2.0, 0.0), 0.000001);
        assertEquals(0.5, MathUtils.reduce(1.5, 2.0, 0.0), 0.000001);
        assertEquals(-1.0, MathUtils.reduce(-1.0, 2.0, 0.0), 0.000001);
        assertEquals(-0.5, MathUtils.reduce(-1.5, 2.0, 0.0), 0.000001);
        assertEquals(3.0, MathUtils.reduce(MathUtils.TWO_PI + 3.0, MathUtils.TWO_PI, 0.0), 0.000001);
        assertEquals(-5.0, MathUtils.reduce(-MathUtils.TWO_PI - 5.0, MathUtils.TWO_PI, 0.0), 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        assertEquals(1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals(-1, MathUtils.copySign((byte) -1, (byte) -1));
        assertEquals(-1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals(1, MathUtils.copySign((byte) -1, (byte) 1));
        try {
            MathUtils.copySign((byte) -128, (byte) 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_short() {
        assertEquals(1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals(-1, MathUtils.copySign((short) -1, (short) -1));
        assertEquals(-1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals(1, MathUtils.copySign((short) -1, (short) 1));
        try {
            MathUtils.copySign((short) -32768, (short) 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_int() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, -1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(-1, 1));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_long() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(-1L, 1L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkNotNull(0.0);
        MathUtils.checkNotNull(1.0);
        MathUtils.checkNotNull(-1.0);
        MathUtils.checkNotNull(Double.MIN_VALUE);
        try {
            MathUtils.checkNotNull(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
        try {
            MathUtils.checkNotNull(Double.NEGATIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
        try {
            MathUtils.checkNotNull(Double.NaN);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] arr1 = {0.0, 1.0, -1.0, Double.MIN_VALUE};
        double[] arr2 = {0.0, 1.0, -1.0, Double.POSITIVE_INFINITY};
        double[] arr3 = {0.0, 1.0, -1.0, Double.NEGATIVE_INFINITY};
        double[] arr4 = {0.0, 1.0, -1.0, Double.NaN};

        MathUtils.checkNotNull(arr1);
        try {
            MathUtils.checkNotNull(arr2);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
        try {
            MathUtils.checkNotNull(arr3);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
        try {
            MathUtils.checkNotNull(arr4);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        MathUtils.checkNotNull(new Object());
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull(new Object());
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertNotNull(e);
        }
    }

}
