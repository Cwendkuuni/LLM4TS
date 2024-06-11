import org.apache.commons.math3.exception.Localizable;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    // Test for hash(double value) method
    @Test
    public void testHash_double() {
        // Typical use case
        double value = 3.14159;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);

        // Edge case
        value = 0.0;
        expected = Double.valueOf(value).hashCode();
        actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);

        // Error scenario
        try {
            MathUtils.hash(Double.NaN);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.NOT_FINITE_NUMBER, e.getPattern());
        }
    }

    // Test for hash(double[] value) method
    @Test
    public void testHash_doubleArray() {
        // Typical use case
        double[] value = {1.1, 2.2, 3.3};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);

        // Edge case
        value = new double[]{};
        expected = Arrays.hashCode(value);
        actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    // Test for normalizeAngle(double a, double center) method
    @Test
    public void testNormalizeAngle() {
        // Typical use case
        double a = 8.0;
        double center = 3.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.00001);

        // Edge case
        a = 0.0;
        center = 0.0;
        expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.00001);
    }

    // Test for reduce(double a, double period, double offset) method
    @Test
    public void testReduce() {
        // Typical use case
        double a = 5.5;
        double period = MathUtils.TWO_PI;
        double offset = 1.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.00001);

        // Edge case
        a = 0.0;
        period = MathUtils.TWO_PI;
        offset = 0.0;
        expected = a - period * Math.floor((a - offset) / period) - offset;
        actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.00001);
    }

    // Test for copySign(byte magnitude, byte sign) method
    @Test
    public void testCopySign_byte() {
        // Typical use case
        byte magnitude = 10;
        byte sign = -1;
        byte expected = (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Edge case
        magnitude = 0;
        sign = 1;
        expected = magnitude;
        actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Error scenario
        try {
            MathUtils.copySign((byte) -128, (byte) 1);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    // Test for copySign(short magnitude, short sign) method
    @Test
    public void testCopySign_short() {
        // Typical use case
        short magnitude = 100;
        short sign = -1;
        short expected = (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Edge case
        magnitude = 0;
        sign = 1;
        expected = magnitude;
        actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Error scenario
        try {
            MathUtils.copySign((short) -32768, (short) 1);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    // Test for copySign(int magnitude, int sign) method
    @Test
    public void testCopySign_int() {
        // Typical use case
        int magnitude = 1000;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Edge case
        magnitude = 0;
        sign = 1;
        expected = magnitude;
        actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Error scenario
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    // Test for copySign(long magnitude, long sign) method
    @Test
    public void testCopySign_long() {
        // Typical use case
        long magnitude = 100000000000L;
        long sign = -1L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Edge case
        magnitude = 0L;
        sign = 1L;
        expected = magnitude;
        actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);

        // Error scenario
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            Assert.fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    // Test for checkFinite(double x) method
    @Test
    public void testCheckFinite_double() {
        // Typical use case
        double x = 5.2;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected NotFiniteNumberException");
        }

        // Error scenario
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.NOT_FINITE_NUMBER, e.getPattern());
        }
    }

    // Test for checkFinite(double[] val) method
    @Test
    public void testCheckFinite_doubleArray() {
        // Typical use case
        double[] val = {1.1, 2.2, 3.3};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected NotFiniteNumberException");
        }

        // Error scenario
        val = new double[]{1.0, 2.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            Assert.assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            Assert.assertEquals(2, e.getArgument());
        }
    }

    // Test for checkNotNull(Object o, Localizable pattern, Object... args) method
    @Test
    public void testCheckNotNull_Localizable() {
        // Typical use case
        Object o = new Object();
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;

        try {
            MathUtils.checkNotNull(o, pattern);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected NullArgumentException");
        }

        // Error scenario
        o = null;
        try {
            MathUtils.checkNotNull(o, pattern);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            Assert.assertEquals(pattern, e.getPattern());
        }
    }

    // Test for checkNotNull(Object o) method
    @Test
    public void testCheckNotNull() {
        // Typical use case
        Object o = new Object();

        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected NullArgumentException");
        }

        // Error scenario
        o = null;

        try {
            MathUtils.checkNotNull(o);
            Assert.fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            Assert.assertEquals(LocalizedFormats.NULL_NOT_ALLOWED, e.getPattern());
        }
    }

}