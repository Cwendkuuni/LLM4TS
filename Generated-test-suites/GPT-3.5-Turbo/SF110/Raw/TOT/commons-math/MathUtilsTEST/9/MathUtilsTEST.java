
import org.apache.commons.math3.exception.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 1.0;
        int hash = MathUtils.hash(value);
        assertEquals(Double.valueOf(value).hashCode(), hash);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int hash = MathUtils.hash(value);
        int expectedHash = Double.hashCode(value[0]);
        expectedHash = 31 * expectedHash + Double.hashCode(value[1]);
        expectedHash = 31 * expectedHash + Double.hashCode(value[2]);
        assertEquals(expectedHash, hash);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 7.0;
        double center = 2.0;
        double normalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals(0.7168146928204138, normalizedAngle, 0.0000001);
    }

    @Test
    public void testReduce() {
        double a = 17.0;
        double period = 5.0;
        double offset = 2.0;
        double reducedValue = MathUtils.reduce(a, period, offset);
        assertEquals(14.0, reducedValue, 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_byte_negative_magnitude() {
        byte magnitude = 10;
        byte sign = -1;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCopySign_byte_positive_magnitude() {
        byte magnitude = 10;
        byte sign = 1;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_short_negative_magnitude() {
        short magnitude = 10000;
        short sign = -1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCopySign_short_positive_magnitude() {
        short magnitude = 10000;
        short sign = 1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_int_negative_magnitude() {
        int magnitude = 1000000;
        int sign = -1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCopySign_int_positive_magnitude() {
        int magnitude = 1000000;
        int sign = 1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySign_long_negative_magnitude() {
        long magnitude = 100000000000L;
        long sign = -1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCopySign_long_positive_magnitude() {
        long magnitude = 100000000000L;
        long sign = 1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleInfinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArrayInfinity() {
        double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArrayNaN() {
        double[] val = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPattern() {
        Object obj = null;
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        MathUtils.checkNotNull(obj, pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withoutPattern() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

}

