import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 12.5;
        int result = MathUtils.hash(value);
        assertEquals(Double.valueOf(value).hashCode(), result);
    }

    @Test
    public void testHashArray() {
        double[] value = {1.2, 3.4, 5.6};
        int result = MathUtils.hash(value);
        assertEquals(Arrays.hashCode(value), result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 4.5;
        double center = 2.0;
        double result = MathUtils.normalizeAngle(a, center);
        double expected = expectedResult(a, center);
        assertEquals(expected, result, 0.000001);
    }
    
    private double expectedResult(double a, double center) {
        return a - MathUtils.TWO_PI * FastMath.floor((a + 3.141592653589793 - center) / MathUtils.TWO_PI);
    }

    @Test
    public void testReduce() {
        double a = 7.5;
        double period = 3.0;
        double offset = 1.0;
        double result = MathUtils.reduce(a, period, offset);
        double expected = expectedReducedResult(a, period, offset);
        assertEquals(expected, result, 0.000001);
    }
    
    private double expectedReducedResult(double a, double period, double offset) {
        double p = FastMath.abs(period);
        return a - p * FastMath.floor((a - offset) / p) - offset;
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            byte expected = expectedCopySignByteResult(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    private byte expectedCopySignByteResult(byte magnitude, byte sign) {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == -128) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return (byte)(-magnitude);
    }

    @Test
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException but no exception was thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            short expected = expectedCopySignShortResult(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    private short expectedCopySignShortResult(short magnitude, short sign) {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == -32768) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return (short)(-magnitude);
    }

    @Test
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        try {
            short result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException but no exception was thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            int expected = expectedCopySignIntResult(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    private int expectedCopySignIntResult(int magnitude, int sign) {
        if ((magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0)) {
            return magnitude;
        }
        if (sign >= 0 && magnitude == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    @Test
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException but no exception was thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000L;
        long sign = -5000L;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            long expected = expectedCopySignLongResult(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    private long expectedCopySignLongResult(long magnitude, long sign) {
        if ((magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L)) {
            return magnitude;
        }
        if (sign >= 0L && magnitude == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -magnitude;
    }

    @Test
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException but no exception was thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }

    @Test
    public void testCheckFinite() {
        double x = 1.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException but no exception was thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getArgument());
        }
    }

    @Test
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException but no exception was thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getArgument());
        }
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCheckFiniteArrayInfinity() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException but no exception was thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(Double.POSITIVE_INFINITY, e.getArgument());
            assertEquals(1, e.getArguments().length);
            assertEquals(1, (int)e.getArguments()[0]);
        }
    }

    @Test
    public void testCheckFiniteArrayNaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException but no exception was thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(Double.NaN, e.getArgument());
            assertEquals(1, e.getArguments().length);
            assertEquals(1, (int)e.getArguments()[0]);
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
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = new Object[] {};
        try {
            MathUtils.checkNotNull(o, pattern, args);
            fail("Expected NullArgumentException but no exception was thrown");
        } catch (NullArgumentException e) {
            assertEquals(pattern, e.getPattern());
            assertEquals(args, e.getArguments());
        }
    }

}