import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;

public class MathUtilsTest {
    
    // Testing hash(double value)
    @Test
    public void testHash_doubleValue() {
        double value = 3.25;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    // Testing hash(double[] value)
    @Test
    public void testHash_doubleArrayValue() {
        double[] value = {2.5, 4.7, 6.9};
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    // Testing normalizeAngle(double a, double center)
    @Test
    public void testNormalizeAngle_positiveAAndCenter() {
        double a = 3.0;
        double center = 1.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testNormalizeAngle_negativeAAndCenter() {
        double a = -2.0;
        double center = -1.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }
    
    // Testing reduce(double a, double period, double offset)
    @Test
    public void testReduce_positiveA() {
        double a = 10.0;
        double period = 2.5;
        double offset = 1.5;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }
    
    @Test
    public void testReduce_negativeA() {
        double a = -8.0;
        double period = 3.2;
        double offset = -2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }
    
    // Testing copySign(byte magnitude, byte sign)
    @Test
    public void testCopySign_sameSign() {
        byte magnitude = 5;
        byte sign = 2;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_differentSign_magnitudeOverflow() {
        byte magnitude = -128;
        byte sign = 3;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySign_differentSign() {
        byte magnitude = -5;
        byte sign = 10;
        byte expected = (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    // Testing copySign(short magnitude, short sign)
    @Test
    public void testCopySign_sameSign_short() {
        short magnitude = 100;
        short sign = 50;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_differentSign_magnitudeOverflow_short() {
        short magnitude = -32768;
        short sign = 60;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySign_differentSign_short() {
        short magnitude = -100;
        short sign = 200;
        short expected = (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    // Testing copySign(int magnitude, int sign)
    @Test
    public void testCopySign_sameSign_int() {
        int magnitude = 500;
        int sign = 200;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_differentSign_magnitudeOverflow_int() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 100;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySign_differentSign_int() {
        int magnitude = -700;
        int sign = 400;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    // Testing copySign(long magnitude, long sign)
    @Test
    public void testCopySign_sameSign_long() {
        long magnitude = 1000000L;
        long sign = 200000L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_differentSign_magnitudeOverflow_long() {
        long magnitude = Long.MIN_VALUE;
        long sign = 5000000L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySign_differentSign_long() {
        long magnitude = -100000L;
        long sign = 200000L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    // Testing checkFinite(double x)
    @Test
    public void testCheckFinite_finiteNumber() {
        double x = 3.14;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Expected NotFiniteNumberException");
        }
    }
    
    @Test
    public void testCheckFinite_infiniteNumber() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getArgument());
        }
    }
    
    @Test
    public void testCheckFinite_nanNumber() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(x, e.getArgument());
        }
    }
    
    // Testing checkFinite(double[] val)
    @Test
    public void testCheckFiniteArray_allFiniteNumbers() {
        double[] val = {1.2, 3.4, -5.6};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Expected NotFiniteNumberException");
        }
    }
    
    @Test
    public void testCheckFiniteArray_inifiniteNumber() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, -5.6};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(val[1], e.getArgument());
            assertEquals(1, e.getIndices()[0]);
        }
    }
    
    @Test
    public void testCheckFiniteArray_nanNumber() {
        double[] val = {1.2, Double.NaN, -5.6};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getPattern());
            assertEquals(val[1], e.getArgument());
            assertEquals(1, e.getIndices()[0]);
        }
    }
    
    // Testing checkNotNull(Object o, Localizable pattern, Object... args)
    @Test
    public void testCheckNotNull_objectNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.OBJECT_CANNOT_BE_NULL, "test");
        } catch (NullArgumentException e) {
            fail("Expected NullArgumentException");
        }
    }
    
    @Test
    public void testCheckNotNull_objectNull() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.OBJECT_CANNOT_BE_NULL, "test");
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.OBJECT_CANNOT_BE_NULL, e.getPattern());
            assertArrayEquals(new Object[] {"test"}, e.getArguments());
        }
    }
    
    // Testing checkNotNull(Object o)
    @Test
    public void testCheckNotNull_objectNotNull_shortMethod() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Expected NullArgumentException");
        }
    }
    
    @Test
    public void testCheckNotNull_objectNull_shortMethod() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.OBJECT_CANNOT_BE_NULL, e.getPattern());
            assertArrayEquals(new Object[0], e.getArguments());
        }
    }

}