
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.NullArgumentException;

public class MathUtilsTest {
    
    @Test
    public void testHash_double_positive() {
        double value = 3.14;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_double_zero() {
        double value = 0.0;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_double_negative() {
        double value = -2.718;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle_positive_centerZero() {
        double a = 3.141592653589793; // pi
        double center = 0.0;
        double expected = -MathUtils.TWO_PI + a;
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testNormalizeAngle_negative_centerZero() {
        double a = -3.141592653589793; // -pi
        double center = 0.0;
        double expected = MathUtils.TWO_PI + a;
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testNormalizeAngle_positive_centerPositive() {
        double a = 3.141592653589793; // pi
        double center = 1.0;
        double expected = -MathUtils.TWO_PI + a + center;
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testNormalizeAngle_negative_centerPositive() {
        double a = -3.141592653589793; // -pi
        double center = 1.0;
        double expected = MathUtils.TWO_PI + a + center;
        double actual = MathUtils.normalizeAngle(a, center);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce_positivePeriod_offsetZero() {
        double a = 10.0;
        double period = 5.0;
        double offset = 0.0;
        double expected = a % period;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce_zeroPeriod_positiveOffset() {
        double a = 10.0;
        double period = 0.0;
        double offset = 5.0;
        double expected = a - offset;
        double actual = MathUtils.reduce(a, period, offset);
        Assert.assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySign_sameSign() {
        byte magnitude = 127;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_differentSign() {
        byte magnitude = 127;
        byte sign = -1;
        byte expected = -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        byte actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short_sameSign() {
        short magnitude = 32767;
        short sign = 1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short_differentSign() {
        short magnitude = 32767;
        short sign = -1;
        short expected = -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        short actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int_sameSign() {
        int magnitude = 2147483647;
        int sign = 1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int_differentSign() {
        int magnitude = 2147483647;
        int sign = -1;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        int actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long_sameSign() {
        long magnitude = 9223372036854775807L;
        long sign = 1L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long_differentSign() {
        long magnitude = 9223372036854775807L;
        long sign = -1L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        long actual = MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_validNumber() {
        double x = 5.0;
        MathUtils.checkFinite(x);
        // no exception thrown, test passes
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_infiniteNumber() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_NaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray_validNumbers() {
        double[] arr = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(arr);
        // no exception thrown, test passes
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_infiniteNumber() {
        double[] arr = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(arr);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_NaN() {
        double[] arr = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(arr);
    }
    
    @Test
    public void testCheckNotNull_withObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
        // no exception thrown, test passes
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
    
    @Test
    public void testCheckNotNull_withObjectAndPattern() {
        Object obj = new Object();
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(obj, pattern, args);
        // no exception thrown, test passes
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withNullObjectAndPattern() {
        Object obj = null;
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(obj, pattern, args);
    }
}

