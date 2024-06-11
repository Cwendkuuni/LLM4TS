
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 2*MathUtils.TWO_PI + Math.PI/2;
        double center = Math.PI;
        double expected = Math.PI/2;
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        double expected = 0.0;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -1;
        byte expected = -10;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);

            magnitude = -128;
            actual = MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected exception not thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -1;
        short expected = -100;
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);

            magnitude = -32768;
            actual = MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected exception not thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -1;
        int expected = -1000;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);

            magnitude = Integer.MIN_VALUE;
            actual = MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected exception not thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 1000000L;
        long sign = -1L;
        long expected = -1000000L;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, actual);

            magnitude = Long.MIN_VALUE;
            actual = MathUtils.copySign(magnitude, sign);
            Assert.fail("Expected exception not thrown");
        } catch (MathArithmeticException e) {
            Assert.assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite_double() {
        double x = 10.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception thrown");
        }

        x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected exception not thrown");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            Assert.fail("Expected exception not thrown");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected exception thrown");
        }

        val = new double[]{1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected exception not thrown");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }

        val = new double[]{1.0, 2.0, Double.NaN};
        try {
            MathUtils.checkFinite(val);
            Assert.fail("Expected exception not thrown");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckNotNull() {
        Object obj = new Object();
        try {
            MathUtils.checkNotNull(obj);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception thrown");
        }

        obj = null;
        try {
            MathUtils.checkNotNull(obj);
            Assert.fail("Expected exception not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckNotNull_pattern() {
        Object obj = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        try {
            MathUtils.checkNotNull(obj, pattern);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected exception thrown");
        }

        obj = null;
        try {
            MathUtils.checkNotNull(obj, pattern);
            Assert.fail("Expected exception not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
            Assert.assertEquals(pattern, e.getPattern());
        }
    }
}

