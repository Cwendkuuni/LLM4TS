
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(5, MathUtils.hash(5.0));
        assertEquals(-5, MathUtils.hash(-5.0));
        assertEquals(10, MathUtils.hash(10.0));
    }

    @Test
    public void testHash_doubleArray() {
        assertEquals(0, MathUtils.hash(new double[] {}));
        assertEquals(5, MathUtils.hash(new double[] { 5.0 }));
        assertEquals(-5, MathUtils.hash(new double[] { -5.0 }));
        assertEquals(10, MathUtils.hash(new double[] { 10.0 }));
        assertEquals(15, MathUtils.hash(new double[] { 5.0, 10.0 }));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 0.0), 0.0);
        assertEquals(1.0, MathUtils.normalizeAngle(7.283185307179586, 0.0), 0.0);
        assertEquals(2.0, MathUtils.normalizeAngle(8.283185307179586, 0.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 1.0), 0.0);
        assertEquals(1.0, MathUtils.normalizeAngle(1.0, 1.0), 0.0);
        assertEquals(0.0, MathUtils.normalizeAngle(2.0, 1.0), 0.0);
        assertEquals(1.0, MathUtils.normalizeAngle(3.0, 1.0), 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(-3.0, MathUtils.reduce(-3.0, 6.0, 0.0), 0.0);
        assertEquals(3.0, MathUtils.reduce(9.0, 6.0, 0.0), 0.0);
        assertEquals(0.0, MathUtils.reduce(0.0, 6.0, 0.0), 0.0);
        assertEquals(2.0, MathUtils.reduce(8.0, 6.0, 1.0), 0.0);
    }

    @Test
    public void testCopySign_byte() throws MathArithmeticException {
        assertEquals(Byte.MAX_VALUE, MathUtils.copySign(Byte.MAX_VALUE, (byte) 1));
        assertEquals(Byte.MIN_VALUE + 1, MathUtils.copySign(Byte.MIN_VALUE, (byte) 1));
        assertEquals(Byte.MIN_VALUE, MathUtils.copySign((byte) 1, (byte) -1));
        try {
            MathUtils.copySign((byte) 1, (byte) 1);
            fail("MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_short() throws MathArithmeticException {
        assertEquals(Short.MAX_VALUE, MathUtils.copySign(Short.MAX_VALUE, (short) 1));
        assertEquals(Short.MIN_VALUE + 1, MathUtils.copySign(Short.MIN_VALUE, (short) 1));
        assertEquals(Short.MIN_VALUE, MathUtils.copySign((short) 1, (short) -1));
        try {
            MathUtils.copySign((short) 1, (short) 1);
            fail("MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_int() throws MathArithmeticException {
        assertEquals(Integer.MAX_VALUE, MathUtils.copySign(Integer.MAX_VALUE, 1));
        assertEquals(Integer.MIN_VALUE + 1, MathUtils.copySign(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MIN_VALUE, MathUtils.copySign(1, -1));
        try {
            MathUtils.copySign(1, 1);
            fail("MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals(Long.MAX_VALUE, MathUtils.copySign(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE + 1, MathUtils.copySign(Long.MIN_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, MathUtils.copySign(1L, -1L));
        try {
            MathUtils.copySign(1L, 1L);
            fail("MathArithmeticException not thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(Double.MAX_VALUE);
        MathUtils.checkFinite(Double.MIN_VALUE);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
        try {
            MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
        try {
            MathUtils.checkFinite(Double.NaN);
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        MathUtils.checkFinite(new double[] {});
        MathUtils.checkFinite(new double[] { 0.0 });
        MathUtils.checkFinite(new double[] { 1.0 });
        MathUtils.checkFinite(new double[] { -1.0 });
        MathUtils.checkFinite(new double[] { Double.MAX_VALUE });
        MathUtils.checkFinite(new double[] { Double.MIN_VALUE });
        try {
            MathUtils.checkFinite(new double[] { Double.POSITIVE_INFINITY });
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
        try {
            MathUtils.checkFinite(new double[] { Double.NEGATIVE_INFINITY });
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
        try {
            MathUtils.checkFinite(new double[] { Double.NaN });
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
        try {
            MathUtils.checkFinite(new double[] { Double.MAX_VALUE, Double.POSITIVE_INFINITY });
            fail("NotFiniteNumberException not thrown");
        } catch (NotFiniteNumberException e) {
            assertNotNull(e.getPattern());
        }
    }

    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.UNKNOWN_LOCALIZATION_KEY);
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.UNKNOWN_LOCALIZATION_KEY);
            fail("NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            assertNotNull(e.getPattern());
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        MathUtils.checkNotNull(new Object());
        try {
            MathUtils.checkNotNull(null);
            fail("NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            assertNotNull(e.getPattern());
        }
    }

}
