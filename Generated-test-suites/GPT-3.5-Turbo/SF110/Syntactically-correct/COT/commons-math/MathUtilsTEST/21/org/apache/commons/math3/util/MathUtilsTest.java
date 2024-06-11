import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 2.0;
        double center = 0.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 5.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = -1;
        byte sign = 1;
        byte expected = (byte)(-magnitude);
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
        short magnitude = -1;
        short sign = 1;
        short expected = (short)(-magnitude);
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
        int magnitude = -1;
        int sign = 1;
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
        long magnitude = -1L;
        long sign = 1L;
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
        double x = 10.5;
        MathUtils.checkFinite(x);
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
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_infinite() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_nan() {
        double[] val = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull_object_localizable() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.MSG;
        Object[] args = {};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_object_localizable_null() {
        Object o = null;
        Localizable pattern = LocalizedFormats.MSG;
        Object[] args = {};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test
    public void testCheckNotNull_object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_object_null() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}