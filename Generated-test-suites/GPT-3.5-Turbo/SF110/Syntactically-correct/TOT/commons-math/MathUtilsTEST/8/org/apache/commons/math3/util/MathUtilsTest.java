import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_singleValue() {
        double value = 10.0;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHash_array() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle_positiveCenter() {
        double a = 5.5;
        double center = 2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testNormalizeAngle_negativeCenter() {
        double a = 4.2;
        double center = -2.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testReduce_positivePeriod() {
        double a = 10.5;
        double period = 3.0;
        double offset = 2.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testReduce_negativePeriod() {
        double a = 12.7;
        double period = -4.0;
        double offset = 1.5;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testCopySign_byte_sameSign() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = 2;
        byte expected = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_byte_differentSign_positiveMagnitude() throws MathArithmeticException {
        byte magnitude = 10;
        byte sign = -2;
        byte expected = -magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_byte_differentSign_negativeMagnitude() throws MathArithmeticException {
        byte magnitude = -10;
        byte sign = 2;
        byte expected = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_byte_overflow() {
        byte magnitude = -128;
        byte sign = 2;
        
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_short_sameSign() throws MathArithmeticException {
        short magnitude = 100;
        short sign = 20;
        short expected = magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_short_differentSign_positiveMagnitude() throws MathArithmeticException {
        short magnitude = 100;
        short sign = -20;
        short expected = -magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_short_differentSign_negativeMagnitude() throws MathArithmeticException {
        short magnitude = -100;
        short sign = 20;
        short expected = magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_short_overflow() {
        short magnitude = -32768;
        short sign = 20;
        
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_int_sameSign() throws MathArithmeticException {
        int magnitude = 1000;
        int sign = 200;
        int expected = magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_int_differentSign_positiveMagnitude() throws MathArithmeticException {
        int magnitude = 1000;
        int sign = -200;
        int expected = -magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_int_differentSign_negativeMagnitude() throws MathArithmeticException {
        int magnitude = -1000;
        int sign = 200;
        int expected = magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_int_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 200;
        
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_long_sameSign() throws MathArithmeticException {
        long magnitude = 1000000000L;
        long sign = 200000000L;
        long expected = magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_long_differentSign_positiveMagnitude() throws MathArithmeticException {
        long magnitude = 1000000000L;
        long sign = -200000000L;
        long expected = -magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_long_differentSign_negativeMagnitude() throws MathArithmeticException {
        long magnitude = -1000000000L;
        long sign = 200000000L;
        long expected = magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCopySign_long_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 200000000L;
        
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCheckFinite_finiteNumber() throws NotFiniteNumberException {
        double number = 5.7;
        
        MathUtils.checkFinite(number);
        
        // No exception thrown, test passes
    }
    
    @Test
    public void testCheckFinite_infiniteNumber() {
        double number = Double.POSITIVE_INFINITY;
        
        try {
            MathUtils.checkFinite(number);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Exception thrown, test passes
        }
    }
    
    @Test
    public void testCheckFinite_NaN() {
        double number = Double.NaN;
        
        try {
            MathUtils.checkFinite(number);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Exception thrown, test passes
        }
    }
    
    @Test
    public void testCheckFinite_array_allFinite() throws NotFiniteNumberException {
        double[] array = {-1.5, 0.0, 2.3, -4.1};
        
        MathUtils.checkFinite(array);
        
        // No exception thrown, test passes
    }
    
    @Test
    public void testCheckFinite_array_oneInfinite() {
        double[] array = {-1.5, Double.POSITIVE_INFINITY, 2.3, -4.1};
        
        try {
            MathUtils.checkFinite(array);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Exception thrown, test passes
        }
    }
    
    @Test
    public void testCheckFinite_array_oneNaN() {
        double[] array = {-1.5, Double.NaN, 2.3, -4.1};
        
        try {
            MathUtils.checkFinite(array);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Exception thrown, test passes
        }
    }
    
    @Test
    public void testCheckNotNull_notNull() throws NullArgumentException {
        Object obj = new Object();
        
        MathUtils.checkNotNull(obj);
        
        // No exception thrown, test passes
    }
    
    @Test
    public void testCheckNotNull_null() {
        Object obj = null;
        
        try {
            MathUtils.checkNotNull(obj);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException ex) {
            // Exception thrown, test passes
        }
    }
    
    @Test
    public void testCheckNotNull_withPattern_notNull() throws NullArgumentException {
        Object obj = new Object();
        Localizable pattern = LocalizedFormats.UNSUPPORTED_OPERATION;
        Object[] args = {"argument"};
        
        MathUtils.checkNotNull(obj, pattern, args);
        
        // No exception thrown, test passes
    }
    
    @Test
    public void testCheckNotNull_withPattern_null() {
        Object obj = null;
        Localizable pattern = LocalizedFormats.UNSUPPORTED_OPERATION;
        Object[] args = {"argument"};
        
        try {
            MathUtils.checkNotNull(obj, pattern, args);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException ex) {
            // Exception thrown, test passes
        }
    }
}