import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 4.2;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {1.2, 3.4, 5.6};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 3.7;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.5;
        double offset = 1.3;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -3;
        byte expected = (byte) (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = (short) (magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = magnitude >= 0 && sign >= 0 || magnitude < 0 && sign < 0 ? magnitude : -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = 100000;
        long sign = -50000;
        long expected = magnitude >= 0L && sign >= 0L || magnitude < 0L && sign < 0L ? magnitude : -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFiniteSingleValue() {
        double x = 2.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException ex) {
            fail("NotFiniteNumberException should not be thrown.");
        }
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
    public void testCheckFiniteArrayAllFinite() {
        double[] val = {1.2, 3.4, -5.6};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException ex) {
            fail("NotFiniteNumberException should not be thrown.");
        }
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayContainsInfinity() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, -5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayContainsNaN() {
        double[] val = {1.2, Double.NaN, -5.6};
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNullWithPattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {0};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException ex) {
            fail("NullArgumentException should not be thrown.");
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPatternNullObject() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {0};
        MathUtils.checkNotNull(o, pattern, args);
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException ex) {
            fail("NullArgumentException should not be thrown.");
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

}