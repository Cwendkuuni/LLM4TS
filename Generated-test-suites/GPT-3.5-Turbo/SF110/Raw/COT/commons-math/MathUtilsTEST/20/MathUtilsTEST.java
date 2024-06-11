
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        double value = 10.5;
        int expected = Double.valueOf(10.5).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {10.5, 20.2, 30.8};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 5.0;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 5.0;
        double offset = 2.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteException() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = -30000;
        short sign = -20000;
        short expected = -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortException() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = -1000000;
        int sign = -2000000;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntException() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = -1234567890L;
        long sign = -9876543210L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongException() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite() {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteException() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray() {
        double[] val = {10.5, 20.2, 30.8};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayException() {
        double[] val = {10.5, Double.NEGATIVE_INFINITY, 30.8};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullException() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
    
    @Test
    public void testCheckNotNullPattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = new Object[0];
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternException() {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = new Object[0];
        MathUtils.checkNotNull(o, pattern, args);
    }
}

