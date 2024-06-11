
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_singleValue() {
        double value = 3.141592653589793;
        int expected = -782935854;
        
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHash_array() {
        double[] value = {-1.0, 2.0, 3.0, 4.0};
        int expected = -1292249819;
        
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = -5.0;
        double center = 0.0;
        double expected = 1.0;
        
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testReduce() {
        double a = 12.5;
        double period = 6.0;
        double offset = -1.0;
        double expected = 5.5;
        
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.000001);
    }
    
    @Test
    public void testCopySign_byte_positive() {
        byte magnitude = 10;
        byte sign = 1;
        byte expected = 10;
        
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_byte_negative() {
        byte magnitude = 10;
        byte sign = -1;
        byte expected = -10;
        
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_short_positive() {
        short magnitude = 100;
        short sign = 1;
        short expected = 100;
        
        short result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_short_negative() {
        short magnitude = 100;
        short sign = -1;
        short expected = -100;
        
        short result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_int_positive() {
        int magnitude = 1000;
        int sign = 1;
        int expected = 1000;
        
        int result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_int_negative() {
        int magnitude = 1000;
        int sign = -1;
        int expected = -1000;
        
        int result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCopySign_long_positive() {
        long magnitude = 1000000000L;
        long sign = -1L;
        long expected = -1000000000L;
        
        long result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_long_negative() {
        long magnitude = 1000000000L;
        long sign = -1L;
        long expected = -1000000000L;
        
        long result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test
    public void testCheckFinite_validNumber() {
        double x = 5.0;
        
        MathUtils.checkFinite(x);
        
        // No exception thrown
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_infinite() {
        double x = Double.POSITIVE_INFINITY;
        
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_nan() {
        double x = Double.NaN;
        
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteArray_validNumbers() {
        double[] val = {1.0, 2.0, 3.0, 4.0};
        
        MathUtils.checkFinite(val);
        
        // No exception thrown
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_infiniteElement() {
        double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY, 4.0};
        
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArray_nanElement() {
        double[] val = {1.0, 2.0, Double.NaN, 4.0};
        
        MathUtils.checkFinite(val);
    }
    
    @Test
    public void testCheckNotNull_notNull() {
        Object o = new Object();
        
        MathUtils.checkNotNull(o);
        
        // No exception thrown
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null() {
        Object o = null;
        
        MathUtils.checkNotNull(o);
    }
    
    @Test
    public void testCheckNotNull_withPattern_notNull() {
        Object o = new Object();
        Localizable pattern = null;
        Object[] args = null;
        
        MathUtils.checkNotNull(o, pattern, args);
        
        // No exception thrown
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPattern_null() {
        Object o = null;
        Localizable pattern = null;
        Object[] args = null;
        
        MathUtils.checkNotNull(o, pattern, args);
    }
}
