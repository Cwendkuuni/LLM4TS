import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14159265359;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = Math.PI;
        double center = 0.0;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 7.0;
        double offset = -4.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = -10;
        byte sign = 5;
        byte expected = (byte)(-magnitude);
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 5;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = -1000;
        short sign = 5000;
        short expected = (short)(-magnitude);
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 5000;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = -100000;
        int sign = 500000;
        int expected = -magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 500000;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = -10000000000L;
        long sign = 50000000000L;
        long expected = -magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 50000000000L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 10.0;
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
        double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_nan() {
        double[] val = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull_withPattern() {
        Object obj = new Object();
        Localizable pattern = LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN;
        Object[] args = {1, 10};
        MathUtils.checkNotNull(obj, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPattern_null() {
        Object obj = null;
        Localizable pattern = LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN;
        Object[] args = {1, 10};
        MathUtils.checkNotNull(obj, pattern, args);
    }
    
    @Test
    public void testCheckNotNull() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
}