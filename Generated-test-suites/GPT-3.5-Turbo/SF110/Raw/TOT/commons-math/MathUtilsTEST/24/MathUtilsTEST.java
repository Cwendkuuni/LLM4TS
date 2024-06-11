import static org.junit.Assert.*;
import org.junit.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_singleValue() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_arrayValues() {
        double[] values = {2.5, 4.7, 6.1};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle_positiveAngle() {
        double a = 5.5;
        double center = 2.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testNormalizeAngle_negativeAngle() {
        double a = -2.0;
        double center = 0.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce_positiveA() {
        double a = 5.6;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce_negativeA() {
        double a = -3.0;
        double period = 5.0;
        double offset = 2.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySign_positiveMagnitude_positiveSign() {
        byte magnitude = 10;
        byte sign = 8;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_negativeMagnitude_negativeSign() {
        short magnitude = -256;
        short sign = -512;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_overflowWithPositiveSign() {
        byte magnitude = -128;
        byte sign = 4;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_validFiniteNumber() {
        double x = 3.5;
        MathUtils.checkFinite(x);
        // no exception should be thrown
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_positiveInfinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_negativeInfinity() {
        double x = Double.NEGATIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_nan() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray_validFiniteNumbers() {
        double[] values = {2.5, 4.7, 6.1};
        MathUtils.checkFinite(values);
        // no exception should be thrown
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_withInfinity() {
        double[] values = {2.5, Double.POSITIVE_INFINITY, 6.1};
        MathUtils.checkFinite(values);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_withNaN() {
        double[] values = {2.5, Double.NaN, 6.1};
        MathUtils.checkFinite(values);
    }
    
    @Test
    public void testCheckNotNull_notNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
        // no exception should be thrown
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObject_defaultMessage() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObject_withPattern() {
        Object obj = null;
        MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_PARAMETER);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObject_withPatternAndArgs() {
        Object obj = null;
        MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_PARAMETER, "obj");
    }
}