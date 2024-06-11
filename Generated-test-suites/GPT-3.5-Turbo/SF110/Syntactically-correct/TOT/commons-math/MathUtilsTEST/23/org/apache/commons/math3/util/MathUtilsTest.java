import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {
    
    // Method: hash(double value)
    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        Assert.assertEquals(expected, result);
    }
    
    // Method: hash(double[] value)
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.2, 3.4, 5.6};
        int expected = java.util.Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        Assert.assertEquals(expected, result);
    }
    
    // Method: normalizeAngle(double a, double center)
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 5.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, result, 0);
    }
    
    // Method: reduce(double a, double period, double offset)
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 4.0;
        double offset = 1.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, result, 0);
    }
    
    // Method: copySign(byte magnitude, byte sign)
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -1;
        byte expected = (byte) -magnitude;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected MathArithmeticException occurred");
        }
    }
    
    // Method: copySign(short magnitude, short sign)
    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -1;
        short expected = (short) -magnitude;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected MathArithmeticException occurred");
        }
    }
    
    // Method: copySign(int magnitude, int sign)
    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -1;
        int expected = -magnitude;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected MathArithmeticException occurred");
        }
    }
    
    // Method: copySign(long magnitude, long sign)
    @Test
    public void testCopySign_long() {
        long magnitude = 100000;
        long sign = -1;
        long expected = -magnitude;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            Assert.assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            Assert.fail("Unexpected MathArithmeticException occurred");
        }
    }
    
    // Method: checkFinite(double x)
    @Test
    public void testCheckFinite_double() {
        double x = 10.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected NotFiniteNumberException occurred");
        }
    }
    
    // Method: checkFinite(double[] val)
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.2, 3.4, 5.6};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            Assert.fail("Unexpected NotFiniteNumberException occurred");
        }
    }
    
    // Method: checkNotNull(Object o, Localizable pattern, Object... args)
    @Test
    public void testCheckNotNull_Localizable() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.ALL_ZERO_ROWS_IN_MATRIX;
        Object[] args = {1, 2};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected NullArgumentException occurred");
        }
    }
    
    // Method: checkNotNull(Object o)
    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            Assert.fail("Unexpected NullArgumentException occurred");
        }
    }
}