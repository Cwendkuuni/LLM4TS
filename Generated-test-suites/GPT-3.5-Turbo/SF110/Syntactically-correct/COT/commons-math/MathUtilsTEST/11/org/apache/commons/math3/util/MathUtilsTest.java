
import org.junit.Test;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.util.MathUtils;

import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 2.0;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {2.0, 3.0, 4.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 20.0;
        double center = 10.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 20.0;
        double period = 10.0;
        double offset = 5.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 2;
        byte sign = -2;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 2;
        short sign = -2;
        short expected = -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 2;
        int sign = -2;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 2L;
        long sign = -2L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite_double() {
        double x = 2.0;
        MathUtils.checkFinite(x);
        // No exception should be thrown
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_infinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_nan() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {2.0, 3.0, 4.0};
        MathUtils.checkFinite(val);
        // No exception should be thrown
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_infinite() {
        double[] val = {2.0, Double.POSITIVE_INFINITY, 4.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_nan() {
        double[] val = {2.0, Double.NaN, 4.0};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull_Localizable() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.PATTERN;
        Object[] args = {"arg1", "arg2"};
        MathUtils.checkNotNull(o, pattern, args);
        // No exception should be thrown
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Localizable_null() {
        Object o = null;
        Localizable pattern = LocalizedFormats.PATTERN;
        Object[] args = {"arg1", "arg2"};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        // No exception should be thrown
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_null() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

}

