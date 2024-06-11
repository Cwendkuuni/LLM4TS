import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHash_double(){
        double value = 3.141592653589793;
        int expected = 1072029096;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray(){
        double[] value = {1.0, 2.0, 3.0};
        int expected = -1309378031;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle1(){
        double a = 7.0;
        double center = 3.0;
        double expected = 3.0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testNormalizeAngle2(){
        double a = 0.0;
        double center = 3.0;
        double expected = -3.141592653589793;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce1(){
        double a = 13.5;
        double period = 7.0;
        double offset = 3.0;
        double expected = 6.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testReduce2(){
        double a = 5.0;
        double period = -1.0;
        double offset = 0.0;
        double expected = 5.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }
    
    @Test
    public void testCopySign_byte1(){
        byte magnitude = 5;
        byte sign = 2;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_byte2(){
        byte magnitude = -128;
        byte sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_short1(){
        short magnitude = 10;
        short sign = -2;
        short expected = 10;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short2(){
        short magnitude = -32768;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_int1(){
        int magnitude = 100;
        int sign = -5;
        int expected = 100;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int2(){
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCopySign_long1(){
        long magnitude = 9999999999L;
        long sign = -8888888888L;
        long expected = 9999999999L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long2(){
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException ex) {
            assertEquals(LocalizedFormats.OVERFLOW, ex.getPattern());
        }
    }
    
    @Test
    public void testCheckFinite_double(){
        double x = 5.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException ex) {
            fail("Not expected NotFiniteNumberException");
        }
    }
    
    @Test
    public void testCheckFinite_double_NaN(){
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Passed
        }
    }
    
    @Test
    public void testCheckFinite_double_Infinity(){
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            // Passed
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray(){
        double[] val = {1.0, -2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException ex) {
            fail("Not expected NotFiniteNumberException");
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_NaN(){
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, ex.getPattern());
            assertEquals(1, ex.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray_Infinity(){
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException ex) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, ex.getPattern());
            assertEquals(1, ex.getArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull_Localized(){
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.OVERFLOW);
        } catch (NullArgumentException ex) {
            fail("Not expected NullArgumentException");
        }
    }
    
    @Test
    public void testCheckNotNull_Localized_Null(){
        Object o = null;
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.OVERFLOW);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException ex) {
            // Passed
        }
    }
    
    @Test
    public void testCheckNotNull(){
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException ex) {
            fail("Not expected NullArgumentException");
        }
    }
    
    @Test
    public void testCheckNotNull_Null(){
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException ex) {
            // Passed
        }
    }
}