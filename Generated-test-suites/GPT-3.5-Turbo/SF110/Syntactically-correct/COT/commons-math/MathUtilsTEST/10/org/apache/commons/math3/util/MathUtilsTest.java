import static org.junit.Assert.*;
import java.util.Arrays;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        double value = 1.23;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {1.23, 4.56, 7.89};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 1.0;
        double center = 0.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testReduce() {
        double a = 1.0;
        double period = 2.0;
        double offset = 0.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignBytePositive() {
        byte magnitude = 100;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignByteNegative() {
        byte magnitude = 100;
        byte sign = -1;
        byte expected = (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignShortPositive() {
        short magnitude = 1000;
        short sign = 1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignShortNegative() {
        short magnitude = 1000;
        short sign = -1;
        short expected = (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntegerOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignIntegerPositive() {
        int magnitude = 1000000;
        int sign = 1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignIntegerNegative() {
        int magnitude = 1000000;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignLongPositive() {
        long magnitude = 100000000000L;
        long sign = 1;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignLongNegative() {
        long magnitude = 100000000000L;
        long sign = -1;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCheckFinite() {
        double x = 1.0;
        MathUtils.checkFinite(x);
        assertTrue(true);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
        assertTrue(true);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() {
        Object o = null;
        LocalizedFormats pattern = LocalizedFormats.UNKNOWN_FORMAT;
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}