import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class MathUtilsTest {

    @Test
    public void testHash_double_positiveValue() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_double_negativeValue() {
        double value = -10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray_emptyArray() {
        double[] value = new double[0];
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray_nonEmptyArray() {
        double[] value = {1.0, 2.5, 3.8, -4.2};
        int expected = java.util.Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle_angleInRange() {
        double angle = 2.7;
        double center = 1.3;
        double expected = angle - MathUtils.TWO_PI * org.apache.commons.math3.util.FastMath.floor((angle + 3.141592653589793 - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(angle, center);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testNormalizeAngle_angleOutOfPositiveRange() {
        double angle = 7.8;
        double center = 1.3;
        double expected = angle - MathUtils.TWO_PI * org.apache.commons.math3.util.FastMath.floor((angle + 3.141592653589793 - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(angle, center);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testNormalizeAngle_angleOutOfNegativeRange() {
        double angle = -10.2;
        double center = -5.7;
        double expected = angle - MathUtils.TWO_PI * org.apache.commons.math3.util.FastMath.floor((angle + 3.141592653589793 - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(angle, center);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce_valueInsidePeriod() {
        double value = 15.5;
        double period = 10.0;
        double offset = 3.0;
        double expected = value - period * org.apache.commons.math3.util.FastMath.floor((value - offset) / period) - offset;
        double actual = MathUtils.reduce(value, period, offset);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce_valueOutsidePeriod() {
        double value = -12.6;
        double period = 7.5;
        double offset = -3.0;
        double expected = value - period * org.apache.commons.math3.util.FastMath.floor((value - offset) / period) - offset;
        double actual = MathUtils.reduce(value, period, offset);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySign_byte_positiveMagnitude_positiveSign() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 5;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_byte_positiveMagnitude_negativeSign() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_byte_negativeMagnitude_positiveSign() throws MathArithmeticException {
        byte magnitude = -10;
        byte sign = 5;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_maximumMagnitude_positiveSign() throws MathArithmeticException {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short_positiveMagnitude_positiveSign() throws MathArithmeticException {
        short magnitude = 1000;
        short sign = 500;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short_positiveMagnitude_negativeSign() throws MathArithmeticException {
        short magnitude = 1000;
        short sign = -500;
        short expected = (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short_negativeMagnitude_positiveSign() throws MathArithmeticException {
        short magnitude = -1000;
        short sign = 500;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_maximumMagnitude_positiveSign() throws MathArithmeticException {
        short magnitude = Short.MIN_VALUE;
        short sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int_positiveMagnitude_positiveSign() throws MathArithmeticException {
        int magnitude = 100000;
        int sign = 50000;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int_positiveMagnitude_negativeSign() throws MathArithmeticException {
        int magnitude = 100000;
        int sign = -50000;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int_negativeMagnitude_positiveSign() throws MathArithmeticException {
        int magnitude = -100000;
        int sign = 50000;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_maximumMagnitude_positiveSign() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long_positiveMagnitude_positiveSign() throws MathArithmeticException {
        long magnitude = 10000000000L;
        long sign = 5000000000L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long_positiveMagnitude_negativeSign() throws MathArithmeticException {
        long magnitude = 10000000000L;
        long sign = -5000000000L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long_negativeMagnitude_positiveSign() throws MathArithmeticException {
        long magnitude = -10000000000L;
        long sign = 5000000000L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_maximumMagnitude_positiveSign() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_finiteNumber() throws NotFiniteNumberException {
        double number = 5.3;
        
        MathUtils.checkFinite(number);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_infiniteNumber() throws NotFiniteNumberException {
        double number = Double.POSITIVE_INFINITY;
        
        MathUtils.checkFinite(number);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_NaN() throws NotFiniteNumberException {
        double number = Double.NaN;
        
        MathUtils.checkFinite(number);
    }
    
    @Test
    public void testCheckFinite_doubleArray_allFiniteNumbers() throws NotFiniteNumberException {
        double[] array = {1.5, -2.7, 3.8, -4.2};
        
        MathUtils.checkFinite(array);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_oneInfiniteNumber() throws NotFiniteNumberException {
        double[] array = {1.5, -2.7, Double.POSITIVE_INFINITY, -4.2};
        
        MathUtils.checkFinite(array);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_oneNaN() throws NotFiniteNumberException {
        double[] array = {1.5, -2.7, Double.NaN, -4.2};
        
        MathUtils.checkFinite(array);
    }
    
    @Test
    public void testCheckNotNull_notNullObjectWithPattern() throws NullArgumentException {
        Object obj = new Object();
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        
        MathUtils.checkNotNull(obj, pattern, args);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObjectWithPattern() throws NullArgumentException {
        Object obj = null;
        LocalizedFormats pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        
        MathUtils.checkNotNull(obj, pattern, args);
    }
    
    @Test
    public void testCheckNotNull_notNullObject() throws NullArgumentException {
        Object obj = new Object();
        
        MathUtils.checkNotNull(obj);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObject() throws NullArgumentException {
        Object obj = null;
        
        MathUtils.checkNotNull(obj);
    }
}