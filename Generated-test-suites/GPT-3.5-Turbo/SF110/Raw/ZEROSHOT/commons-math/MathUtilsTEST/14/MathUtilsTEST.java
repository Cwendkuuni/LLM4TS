import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHashWithPositiveValue() {
        double value = 10.5;
        int expected = 1072693248;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashWithNegativeValue() {
        double value = -5.5;
        int expected = -1070842880;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashWithEmptyArray() {
        double[] value = {};
        int expected = 31;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashWithArray() {
        double[] value = {1.2, -3.4, 5.6};
        int expected = -272009594;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngleWithPositiveValues() {
        double a = 7.2;
        double center = 3.0;
        double expected = 1.2000000000000028;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test
    public void testNormalizeAngleWithNegativeValues() {
        double a = -4.7;
        double center = -2.0;
        double expected = -0.6999999999999993;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000000000000001);
    }
    
    @Test
    public void testReduceWithPositiveValues() {
        double a = 25.8;
        double period = 6.0;
        double offset = 3.0;
        double expected = 4.800000000000001;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test
    public void testReduceWithNegativeValues() {
        double a = -10.5;
        double period = 3.0;
        double offset = 2.0;
        double expected = -1.5000000000000009;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000000000000001);
    }
    
    @Test
    public void testCopySignWithPositiveValues() {
        byte magnitude = 10;
        byte sign = 7;
        byte expected = 10;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignWithNegativeMagnitude() {
        byte magnitude = -10;
        byte sign = 7;
        byte expected = -10;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithOverflow() {
        byte magnitude = -128;
        byte sign = 7;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignWithPositiveValues() {
        short magnitude = 100;
        short sign = 70;
        short expected = 100;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignWithNegativeMagnitude() {
        short magnitude = -100;
        short sign = 70;
        short expected = -100;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithOverflow() {
        short magnitude = -32768;
        short sign = 70;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignWithPositiveValues() {
        int magnitude = 1000;
        int sign = 700;
        int expected = 1000;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignWithNegativeMagnitude() {
        int magnitude = -1000;
        int sign = 700;
        int expected = -1000;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 700;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignWithPositiveValues() {
        long magnitude = 1000000L;
        long sign = 700000L;
        long expected = 1000000L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignWithNegativeMagnitude() {
        long magnitude = -1000000L;
        long sign = 700000L;
        long expected = -1000000L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignWithOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 700000L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFiniteWithFiniteNumber() {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfiniteNumber() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteWithFiniteNumbers() {
        double[] val = {1.2, -3.4, 5.6};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithInfiniteNumbers() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, 5.6};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteWithNaNs() {
        double[] val = {1.2, Double.NaN, 5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNullWithNonNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObjectAndPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}