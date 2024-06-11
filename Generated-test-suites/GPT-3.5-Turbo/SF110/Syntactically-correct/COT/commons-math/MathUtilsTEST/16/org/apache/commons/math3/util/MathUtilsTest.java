
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        // arrange
        double value = 1.5;
        
        // act
        int result = MathUtils.hash(value);
        
        // assert
        Assert.assertNotEquals(0, result);
    }
    
    @Test
    public void testHash_doubleArray() {
        // arrange
        double[] value = {1.0, 2.0, 3.0};
        
        // act
        int result = MathUtils.hash(value);
        
        // assert
        Assert.assertEquals(Arrays.hashCode(value), result);
    }
    
    @Test
    public void testNormalizeAngle() {
        // arrange
        double a = 4.5;
        double center = 1.0;
        
        // act
        double result = MathUtils.normalizeAngle(a, center);
        
        // assert
        Assert.assertEquals(1.5, result, 0.0001);
    }
    
    @Test
    public void testReduce() {
        // arrange
        double a = 7.5;
        double period = 2.0;
        double offset = 1.0;
        
        // act
        double result = MathUtils.reduce(a, period, offset);
        
        // assert
        Assert.assertEquals(0.5, result, 0.0001);
    }
    
    @Test
    public void testCopySign_byte() {
        // arrange
        byte magnitude = 10;
        byte sign = -5;
        
        // act
        byte result = MathUtils.copySign(magnitude, sign);
        
        // assert
        Assert.assertEquals(-10, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte_overflow() {
        // arrange
        byte magnitude = -128;
        byte sign = 1;
        
        // act
        MathUtils.copySign(magnitude, sign);
        
        // assert
        // expects MathArithmeticException to be thrown
    }
    
    @Test
    public void testCopySign_short() {
        // arrange
        short magnitude = 1000;
        short sign = -500;
        
        // act
        short result = MathUtils.copySign(magnitude, sign);
        
        // assert
        Assert.assertEquals(-1000, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_short_overflow() {
        // arrange
        short magnitude = -32768;
        short sign = 1;
        
        // act
        MathUtils.copySign(magnitude, sign);
        
        // assert
        // expects MathArithmeticException to be thrown
    }
    
    @Test
    public void testCopySign_int() {
        // arrange
        int magnitude = 100000;
        int sign = -50000;
        
        // act
        int result = MathUtils.copySign(magnitude, sign);
        
        // assert
        Assert.assertEquals(-100000, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_int_overflow() {
        // arrange
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        
        // act
        MathUtils.copySign(magnitude, sign);
        
        // assert
        // expects MathArithmeticException to be thrown
    }
    
    @Test
    public void testCopySign_long() {
        // arrange
        long magnitude = 100000L;
        long sign = -50000L;
        
        // act
        long result = MathUtils.copySign(magnitude, sign);
        
        // assert
        Assert.assertEquals(-100000L, result);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySign_long_overflow() {
        // arrange
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        
        // act
        MathUtils.copySign(magnitude, sign);
        
        // assert
        // expects MathArithmeticException to be thrown
    }
    
    @Test
    public void testCheckFinite_double() {
        // arrange
        double x = 10.0;
        
        // act
        MathUtils.checkFinite(x);
        
        // assert
        // no exception expected
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double_notFinite() {
        // arrange
        double x = Double.POSITIVE_INFINITY;
        
        // act
        MathUtils.checkFinite(x);
        
        // assert
        // expects NotFiniteNumberException to be thrown
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        // arrange
        double[] val = {1.0, 2.0, 3.0};
        
        // act
        MathUtils.checkFinite(val);
        
        // assert
        // no exception expected
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray_notFinite() {
        // arrange
        double[] val = {1.0, Double.NEGATIVE_INFINITY, 3.0};
        
        // act
        MathUtils.checkFinite(val);
        
        // assert
        // expects NotFiniteNumberException to be thrown
    }
    
    @Test
    public void testCheckNotNull_withPattern() {
        // arrange
        Object o = new Object();
        LocalizedFormats pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        
        // act
        MathUtils.checkNotNull(o, pattern, args);
        
        // assert
        // no exception expected
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withPattern_null() {
        // arrange
        Object o = null;
        LocalizedFormats pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        
        // act
        MathUtils.checkNotNull(o, pattern, args);
        
        // assert
        // expects NullArgumentException to be thrown
    }
    
    @Test
    public void testCheckNotNull() {
        // arrange
        Object o = new Object();
        
        // act
        MathUtils.checkNotNull(o);
        
        // assert
        // no exception expected
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_null() {
        // arrange
        Object o = null;
        
        // act
        MathUtils.checkNotNull(o);
        
        // assert
        // expects NullArgumentException to be thrown
    }
}
