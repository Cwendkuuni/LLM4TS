package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

public class MathUtilsTest {

    // Test data
    double value = 5.0;
    double[] array = {1.0, 2.0, 3.0};
    double a = 3.14;
    double center = 0.0;
    double period = 2.0;
    double offset = 1.0;
    byte magnitudeByte = 1;
    byte signByte = -1;
    short magnitudeShort = 1;
    short signShort = -1;
    int magnitudeInt = 1;
    int signInt = -1;
    long magnitudeLong = 1;
    long signLong = -1;

    @Before
    public void setUp() {
        // Set up test data
    }

    @Test
    public void testHashDouble() {
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        int expected = java.util.Arrays.hashCode(array);
        int actual = MathUtils.hash(array);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double expected = a - MathUtils.TWO_PI * org.apache.commons.math3.util.FastMath.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double expected = a - period * org.apache.commons.math3.util.FastMath.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        try {
            byte expected = (byte) (signByte >= 0 && magnitudeByte == -128 ? value : -magnitudeByte);
            byte actual = MathUtils.copySign(magnitudeByte, signByte);
            Assert.assertEquals(expected, actual);
        } catch (org.apache.commons.math3.exception.MathArithmeticException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignShort() {
        try {
            short expected = (short) (signShort >= 0 && magnitudeShort == -32768 ? value : -magnitudeShort);
            short actual = MathUtils.copySign(magnitudeShort, signShort);
            Assert.assertEquals(expected, actual);
        } catch (org.apache.commons.math3.exception.MathArithmeticException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignInt() {
        try {
            int expected = (signInt >= 0 && magnitudeInt == Integer.MIN_VALUE ? value : -magnitudeInt);
            int actual = MathUtils.copySign(magnitudeInt, signInt);
            Assert.assertEquals(expected, actual);
        } catch (org.apache.commons.math3.exception.MathArithmeticException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignLong() {
        try {
            long expected = (signLong >= 0L && magnitudeLong == Long.MIN_VALUE ? value : -magnitudeLong);
            long actual = MathUtils.copySign(magnitudeLong, signLong);
            Assert.assertEquals(expected, actual);
        } catch (org.apache.commons.math3.exception.MathArithmeticException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        try {
            MathUtils.checkFinite(value);
        } catch (org.apache.commons.math3.exception.NotFiniteNumberException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        try {
            MathUtils.checkFinite(array);
        } catch (org.apache.commons.math3.exception.NotFiniteNumberException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNull() {
        try {
            MathUtils.checkNotNull(value);
        } catch (org.apache.commons.math3.exception.NullArgumentException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNullPattern() {
        try {
            MathUtils.checkNotNull(value, org.apache.commons.math3.exception.util.Localizable.UNKNOWN_LOCALIZABLE);
        } catch (org.apache.commons.math3.exception.NullArgumentException e){
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}