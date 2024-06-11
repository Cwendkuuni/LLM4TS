import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_double_typicalCase() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray_typicalCase() {
        double[] value = {1.5, 2.5, 3.5};
        int expected = value.hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle_typicalCase() {
        double a = 5.8;
        double center = 3.2;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testReduce_typicalCase() {
        double a = 10.4;
        double period = 5.0;
        double offset = 2.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testCopySign_byte_magnitudePositiveSignPositive() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 5;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_byte_magnitudeNegativeSignNegative() throws MathArithmeticException {
        byte magnitude = -10;
        byte sign = -5;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_magnitudeNegativeSignPositive_overflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short_magnitudePositiveSignPositive() throws MathArithmeticException {
        short magnitude = 100;
        short sign = 50;
        short expected = magnitude;
        short actual = MathUtils.copySign(magniute, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short_magnitudeNegativeSignNegative() throws MathArithmeticException {
        short magnitude = -100;
        short sign = -50;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_magnitudeNegativeSignPositive_overflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int_magnitudePositiveSignPositive() throws MathArithmeticException {
        int magnitude = 1000;
        int sign = 500;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int_magnitudeNegativeSignNegative() throws MathArithmeticException {
        int magnitude = -1000;
        int sign = -500;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_magnitudeNegativeSignPositive_overflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long_magnitudePositiveSignPositive() throws MathArithmeticException {
        long magnitude = 1000000L;
        long sign = 500000L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long_magnitudeNegativeSignNegative() throws MathArithmeticException {
        long magnitude = -1000000L;
        long sign = -500000L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_magnitudeNegativeSignPositive_overflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_double_typicalCase() throws NotFiniteNumberException {
        double x = 10.5;
        MathUtils.checkFinite(x);
        // no exception should be thrown
    }
    
    @Test
    public void testCheckFinite_double_positiveInfinity() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckFinite_double_negativeInfinity() {
        double x = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckFinite_double_nan() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_typicalCase() throws NotFiniteNumberException {
        double[] val = {1.5, 2.5, 3.5};
        MathUtils.checkFinite(val);
        // no exception should be thrown
    }
    
    @Test
    public void testCheckFinite_doubleArray_elemPositiveInfinity() {
        double[] val = {1.5, Double.POSITIVE_INFINITY, 3.5};
        try {
            MathUtils.checkFinite(val);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_elemNegativeInfinity() {
        double[] val = {1.5, Double.NEGATIVE_INFINITY, 3.5};
        try {
            MathUtils.checkFinite(val);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_elemNaN() {
        double[] val = {1.5, Double.NaN, 3.5};
        try {
            MathUtils.checkFinite(val);
            fail("NotFiniteNumberException should be thrown");
        } catch (NotFiniteNumberException e) {
            // success
        }
    }
    
    @Test
    public void testCheckNotNull_withPattern_typicalCase() throws NullArgumentException {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
        MathUtils.checkNotNull(o, pattern);
        // no exception should be thrown
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPattern_null() throws NullArgumentException {
        Object o = null;
        Localizable pattern = LocalizedFormats.NULL_NOT_ALLOWED;
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test
    public void testCheckNotNull_withoutPattern_typicalCase() throws NullArgumentException {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        // no exception should be thrown
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withoutPattern_null() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

}