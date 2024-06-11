import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.141592653589793;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = { 1.0, 2.0, 3.0 };
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 7.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 15.0;
        double period = 10.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte)(-magnitude);
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = -300;
        short sign = 500;
        short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short)(-magnitude);
        try {
            short actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = -500;
        int sign = 1000;
        int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
        try {
            int actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = -10000L;
        long sign = 20000L;
        long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
        try {
            long actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = 10.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteDoubleInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFiniteDoubleNaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] val = { 1.0, 2.0, 3.0 };
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteDoubleArrayInfinity() {
        double[] val = { 1.0, Double.POSITIVE_INFINITY, 3.0 };
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(Double.POSITIVE_INFINITY, e.getNumber());
            assertEquals(1, e.getArguments()[0]);
        }
    }

    @Test
    public void testCheckFiniteDoubleArrayNaN() {
        double[] val = { 1.0, Double.NaN, 3.0 };
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(Double.NaN, e.getNumber());
            assertEquals(1, e.getArguments()[0]);
        }
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckNotNullNull() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testCheckNotNullPattern() {
        Object o = null;
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = { 0 };
        try {
            MathUtils.checkNotNull(o, pattern, args);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals(pattern, e.getPattern());
            assertArrayEquals(args, e.getArguments());
        }
    }

}