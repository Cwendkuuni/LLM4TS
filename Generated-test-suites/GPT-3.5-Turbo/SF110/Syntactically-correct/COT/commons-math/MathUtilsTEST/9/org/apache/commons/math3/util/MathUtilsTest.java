import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        double value = 2.5;
        int expected = -1608536789;
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = 3083218;
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 7.0;
        double center = 3.0;
        double expected = -1.4247779607693797;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 5.0;
        double offset = 2.5;
        double expected = 2.0;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = -123;
        byte sign = 1;
        byte expected = -123;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = -32767;
        short sign = 1;
        short expected = -32767;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = -2147483647;
        int sign = 1;
        int expected = -2147483647;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = -9223372036854775807L;
        long sign = 1L;
        long expected = -9223372036854775807L;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
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
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_infinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_NaN() {
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
    public void testCheckFinite_doubleArray_NaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null_localizable() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {0};
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}