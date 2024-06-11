import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        double value = 10.2;
        int expected = 10.2.hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.2;
        double center = 3.1;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.2;
        double period = 5.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        try {
            byte expected = (byte)(-magnitude);
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected exception MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = 10;
        short sign = -5;
        try {
            short expected = (short)(-magnitude);
            short actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected exception MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = 10;
        int sign = -5;
        try {
            int expected = -magnitude;
            int actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected exception MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = 10L;
        long sign = -5L;
        try {
            long expected = -magnitude;
            long actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected exception MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCheckFinite() {
        double x = 10.2;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected exception NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getNumber(), 0.0001);
        }
    }
    
    @Test
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected exception NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getNumber(), 0.0001);
        }
    }
    
    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.2, 2.3, 3.4};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCheckFiniteArrayInfinity() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, 3.4};
        try {
            MathUtils.checkFinite(val);
            fail("Expected exception NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(val[1], e.getNumber(), 0.0001);
            assertEquals(1, e.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckFiniteArrayNaN() {
        double[] val = {1.2, Double.NaN, 3.4};
        try {
            MathUtils.checkFinite(val);
            fail("Expected exception NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(val[1], e.getNumber(), 0.0001);
            assertEquals(1, e.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.MESSAGE_FORMAT);
            fail("Expected exception NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.MESSAGE_FORMAT, e.getPattern());
        }
    }
}