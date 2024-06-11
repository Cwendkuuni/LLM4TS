
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MathUtilsTest {
    @Test
    public void testHash_withDouble_shouldReturnHashCode() {
        assertEquals(Double.valueOf(0.0).hashCode(), MathUtils.hash(0.0));
        assertEquals(Double.valueOf(1.5).hashCode(), MathUtils.hash(1.5));
        assertEquals(Double.valueOf(-3.7).hashCode(), MathUtils.hash(-3.7));
    }

    @Test
    public void testHash_withDoubleArray_shouldReturnHashCode() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {4.0, 5.0, 6.0};
        
        assertEquals(Arrays.hashCode(arr1), MathUtils.hash(arr1));
        assertEquals(Arrays.hashCode(arr2), MathUtils.hash(arr2));
    }

    @Test
    public void testNormalizeAngle_withAngleAndCenter_shouldReturnNormalizedAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0001);
        assertEquals(1.0, MathUtils.normalizeAngle(7.0, 3.0), 0.0001);
        assertEquals(-3.0, MathUtils.normalizeAngle(-9.0, -3.0), 0.0001);
        assertEquals(4.1, MathUtils.normalizeAngle(10.1, 2.0), 0.0001);
    }

    @Test
    public void testReduce_withValuePeriodAndOffset_shouldReturnReducedValue() {
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.0), 0.0001);
        assertEquals(1.5, MathUtils.reduce(7.5, 2.0, 0.5), 0.0001);
        assertEquals(-1.5, MathUtils.reduce(-6.5, 2.0, -0.5), 0.0001);
        assertEquals(5.1, MathUtils.reduce(16.1, 3.0, 1.0), 0.0001);
    }

    @Test(expected=MathArithmeticException.class)
    public void testCopySign_withByteMagnitudeAndSign_shouldThrowExceptionForOverflow() {
        MathUtils.copySign(Byte.MAX_VALUE, (byte)-1);
    }

    @Test
    public void testCopySign_withByteMagnitudeAndSign_shouldReturnCopiedSign() {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)3));
        assertEquals((byte)-5, MathUtils.copySign((byte)5, (byte)-3));
    }

    @Test(expected=MathArithmeticException.class)
    public void testCopySign_withShortMagnitudeAndSign_shouldThrowExceptionForOverflow() {
        MathUtils.copySign(Short.MAX_VALUE, (short)-1);
    }

    @Test
    public void testCopySign_withShortMagnitudeAndSign_shouldReturnCopiedSign() {
        assertEquals((short)10, MathUtils.copySign((short)10, (short)-7));
        assertEquals((short)-10, MathUtils.copySign((short)10, (short)7));
    }

    @Test(expected=MathArithmeticException.class)
    public void testCopySign_withIntMagnitudeAndSign_shouldThrowExceptionForOverflow() {
        MathUtils.copySign(Integer.MAX_VALUE, -1);
    }

    @Test
    public void testCopySign_withIntMagnitudeAndSign_shouldReturnCopiedSign() {
        assertEquals(20, MathUtils.copySign(20, 12));
        assertEquals(-20, MathUtils.copySign(20, -12));
    }

    @Test(expected=MathArithmeticException.class)
    public void testCopySign_withLongMagnitudeAndSign_shouldThrowExceptionForOverflow() {
        MathUtils.copySign(Long.MAX_VALUE, -1L);
    }
    
    @Test
    public void testCopySign_withLongMagnitudeAndSign_shouldReturnCopiedSign() {
        assertEquals(100L, MathUtils.copySign(100L, 75L));
        assertEquals(-100L, MathUtils.copySign(100L, -75L));
    }

    @Test(expected=NotFiniteNumberException.class)
    public void testCheckFinite_withInfiniteValue_shouldThrowException() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected=NotFiniteNumberException.class)
    public void testCheckFinite_withNaNValue_shouldThrowException() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test
    public void testCheckFinite_withFiniteValue_shouldNotThrowException() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(3.5);
        MathUtils.checkFinite(-2.7);
    }

    @Test(expected=NotFiniteNumberException.class)
    public void testCheckFinite_withArrayContainingInfiniteValue_shouldThrowException() {
        double[] arr = {1.0, 2.0, Double.POSITIVE_INFINITY, 4.0};
        MathUtils.checkFinite(arr);
    }

    @Test(expected=NotFiniteNumberException.class)
    public void testCheckFinite_withArrayContainingNaNValue_shouldThrowException() {
        double[] arr = {1.0, 2.0, Double.NaN, 4.0};
        MathUtils.checkFinite(arr);
    }

    @Test
    public void testCheckFinite_withArrayContainingFiniteValues_shouldNotThrowException() {
        double[] arr = {1.0, 2.0, 3.0, 4.0};
        MathUtils.checkFinite(arr);
    }

    @Test(expected=NullArgumentException.class)
    public void testCheckNotNull_withNullObjectAndPattern_shouldThrowException() {
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
    }

    @Test(expected=NullArgumentException.class)
    public void testCheckNotNull_withNullObject_shouldThrowException() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNull_withNonNullObjectAndPattern_shouldNotThrowException() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, LocalizedFormats.ARRAY_ELEMENT, 1);
    }

    @Test
    public void testCheckNotNull_withNonNullObject_shouldNotThrowException() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}
