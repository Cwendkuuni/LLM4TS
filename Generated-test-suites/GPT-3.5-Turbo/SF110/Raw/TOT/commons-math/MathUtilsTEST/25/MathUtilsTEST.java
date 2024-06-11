import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_singleValue() {
        double value = 1.23;
        int expected = new Double(value).hashCode();
        
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHash_arrayValue() {
        double[] value = {1.23, 4.56, 7.89};
        int expected = Arrays.hashCode(value);
        
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle_positiveAngle() {
        double a = 5.6;
        double center = 3.2;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testNormalizeAngle_negativeAngle() {
        double a = -2.5;
        double center = 3.2;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testReduce_positiveA() {
        double a = 8.9;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testReduce_negativeA() {
        double a = -5.4;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testCopySign_sameSign() {
        byte magnitude = 7;
        byte sign = 3;
        byte expected = magnitude;
        
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_differentSign() {
        byte magnitude = 7;
        byte sign = -3;
        byte expected = (byte)(-magnitude);
        
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_overflowMagnitude() {
        byte magnitude = -128;
        byte sign = 3;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySignOverflow_sign() {
        byte magnitude = 7;
        byte sign = -128;
        byte expected = (byte)(-magnitude);
        
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    // Additional test cases for other copySign methods follow
    
    @Test
    public void testCheckFinite_finiteValue() {
        double x = 3.14;
        
        MathUtils.checkFinite(x);
        
        // If no exception is thrown, the test passes
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_infiniteValue() {
        double x = Double.POSITIVE_INFINITY;
        
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_nanValue() {
        double x = Double.NaN;
        
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFinite_arrayWithFiniteValues() {
        double[] val = {1.23, 4.56, 7.89};
        
        MathUtils.checkFinite(val);
        
        // If no exception is thrown, the test passes
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_arrayWithInfiniteValue() {
        double[] val = {1.23, Double.POSITIVE_INFINITY, 7.89};
        
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_arrayWithNanValue() {
        double[] val = {1.23, Double.NaN, 7.89};
        
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull_notNullObject() {
        Object o = new Object();
        
        MathUtils.checkNotNull(o);
        
        // If no exception is thrown, the test passes
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObjectWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.OVERFLOW;
        
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_nullObject() {
        Object o = null;
        
        MathUtils.checkNotNull(o);
    }

}