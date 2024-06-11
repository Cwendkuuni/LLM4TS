import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathUtilsTest {

    private MathUtils mathUtils;

    @Before
    public void setup() {
        mathUtils = new MathUtils();
    }

    // Test case for hash(double value)
    @Test
    public void testHashWithPositiveValue() {
        double value = 2.5;
        int expectedHash = Double.valueOf(value).hashCode();
        int actualHash = mathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    // Test case for hash(double[] value)
    @Test
    public void testHashWithArrayWithPositiveValues() {
        double[] value = {2.5, 3.7, 1.0};
        int expectedHash = Double.valueOf(value[0]).hashCode() + Double.valueOf(value[1]).hashCode()
                + Double.valueOf(value[2]).hashCode();
        int actualHash = mathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    // Test case for normalizeAngle(double a, double center)
    @Test
    public void testNormalizeAngleWithPositiveValues() {
        double a = 4.0;
        double center = 1.0;
        double expectedNormalizedAngle = a - 2 * Math.PI;
        double actualNormalizedAngle = mathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.00001);
    }

    // Test case for reduce(double a, double period, double offset)
    @Test
    public void testReduceWithPositiveValues() {
        double a = 10.5;
        double period = 4.0;
        double offset = 2.0;
        double expectedReducedValue = a - Math.floor((a - offset) / period) * period - offset;
        double actualReducedValue = mathUtils.reduce(a, period, offset);
        assertEquals(expectedReducedValue, actualReducedValue, 0.00001);
    }

    // Test case for copySign(byte magnitude, byte sign)
    @Test
    public void testCopySignWithPositiveNumbers() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 5;
        byte expectedCopySign = magnitude;
        byte actualCopySign = mathUtils.copySign(magnitude, sign);
        assertEquals(expectedCopySign, actualCopySign);
    }

    // Test case for copySign(byte magnitude, byte sign) with overflow
    @Test
    public void testCopySignWithOverflow() {
        byte magnitude = -128;
        byte sign = 5;
        try {
            mathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals("The byte arithmetic operation resulted in an overflow", e.getMessage());
        }
    }

    // Test case for copySign(short magnitude, short sign)
    @Test
    public void testCopySignForShortWithPositiveNumbers() throws MathArithmeticException {
        short magnitude = 100;
        short sign = 50;
        short expectedCopySign = magnitude;
        short actualCopySign = mathUtils.copySign(magnitude, sign);
        assertEquals(expectedCopySign, actualCopySign);
    }

    // Test case for copySign(short magnitude, short sign) with overflow
    @Test
    public void testCopySignForShortWithOverflow() {
        short magnitude = -32768;
        short sign = 50;
        try {
            mathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals("The short arithmetic operation resulted in an overflow", e.getMessage());
        }
    }

    // Test case for copySign(int magnitude, int sign)
    @Test
    public void testCopySignForIntWithPositiveNumbers() throws MathArithmeticException {
        int magnitude = 1000;
        int sign = 500;
        int expectedCopySign = magnitude;
        int actualCopySign = mathUtils.copySign(magnitude, sign);
        assertEquals(expectedCopySign, actualCopySign);
    }

    // Test case for copySign(int magnitude, int sign) with overflow
    @Test
    public void testCopySignForIntWithOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 500;
        try {
            mathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals("The int arithmetic operation resulted in an overflow", e.getMessage());
        }
    }

    // Test case for copySign(long magnitude, long sign)
    @Test
    public void testCopySignForLongWithPositiveNumbers() throws MathArithmeticException {
        long magnitude = 100000L;
        long sign = 50000L;
        long expectedCopySign = magnitude;
        long actualCopySign = mathUtils.copySign(magnitude, sign);
        assertEquals(expectedCopySign, actualCopySign);
    }

    // Test case for copySign(long magnitude, long sign) with overflow
    @Test
    public void testCopySignForLongWithOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 50000L;
        try {
            mathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals("The long arithmetic operation resulted in an overflow", e.getMessage());
        }
    }

    // Test case for checkFinite(double x) with finite value
    @Test
    public void testCheckFiniteWithFiniteValue() throws NotFiniteNumberException {
        double x = 5.6;
        mathUtils.checkFinite(x);
        // No exception is expected, so test passes if no exception is thrown
    }

    // Test case for checkFinite(double x) with infinite value
    @Test
    public void testCheckFiniteWithInfiniteValue() {
        double x = Double.POSITIVE_INFINITY;
        try {
            mathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals("The number is not finite: " + x, e.getMessage());
        }
    }

    // Test case for checkFinite(double x) with NaN value
    @Test
    public void testCheckFiniteWithNaNValue() {
        double x = Double.NaN;
        try {
            mathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals("The number is not finite: " + x, e.getMessage());
        }
    }

    // Test case for checkFinite(double[] val) with all finite values
    @Test
    public void testCheckFiniteWithArrayWithFiniteValues() throws NotFiniteNumberException {
        double[] val = {1.0, 2.0, 3.0};
        mathUtils.checkFinite(val);
        // No exception is expected, so test passes if no exception is thrown
    }

    // Test case for checkFinite(double[] val) with at least one infinite value
    @Test
    public void testCheckFiniteWithArrayWithInfiniteValue() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            mathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals("The element at index 1 is not a finite number: " + val[1], e.getMessage());
        }
    }

    // Test case for checkFinite(double[] val) with at least one NaN value
    @Test
    public void testCheckFiniteWithArrayWithNaNValue() {
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            mathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals("The element at index 1 is not a finite number: " + val[1], e.getMessage());
        }
    }

    // Test case for checkNotNull(Object o, Localizable pattern, Object... args) with non-null object
    @Test
    public void testCheckNotNullWithNonNullObject() throws NullArgumentException {
        Object o = new Object();
        mathUtils.checkNotNull(o, null);
        // No exception is expected, so test passes if no exception is thrown
    }

    // Test case for checkNotNull(Object o, Localizable pattern, Object... args) with null object
    @Test
    public void testCheckNotNullWithNullObject() {
        Object o = null;
        try {
            mathUtils.checkNotNull(o, null);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals("Argument cannot be null", e.getMessage());
        }
    }

    // Test case for checkNotNull(Object o) with non-null object
    @Test
    public void testCheckNotNullWithoutPatternWithNonNullObject() throws NullArgumentException {
        Object o = new Object();
        mathUtils.checkNotNull(o);
        // No exception is expected, so test passes if no exception is thrown
    }

    // Test case for checkNotNull(Object o) with null object
    @Test
    public void testCheckNotNullWithoutPatternWithNullObject() {
        Object o = null;
        try {
            mathUtils.checkNotNull(o);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals("null", e.getMessage());
        }
    }

}