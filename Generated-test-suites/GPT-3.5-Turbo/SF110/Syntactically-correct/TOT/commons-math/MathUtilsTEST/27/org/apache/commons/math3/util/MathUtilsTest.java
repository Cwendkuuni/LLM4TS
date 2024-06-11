import org.apache.commons.math3.exception.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value1 = 3.14;
        int expected1 = Double.valueOf(value1).hashCode();
        int actual1 = MathUtils.hash(value1);
        assertEquals(expected1, actual1);
        
        double value2 = -2.718;
        int expected2 = Double.valueOf(value2).hashCode();
        int actual2 = MathUtils.hash(value2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value1 = {1.0, 2.0, 3.0};
        int expected1 = Arrays.hashCode(value1);
        int actual1 = MathUtils.hash(value1);
        assertEquals(expected1, actual1);
        
        double[] value2 = {-1.0, -2.0, -3.0};
        int expected2 = Arrays.hashCode(value2);
        int actual2 = MathUtils.hash(value2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void testNormalizeAngle() {
        double angle1 = 7.1;
        double center1 = 2.0;
        double expected1 = angle1 - 6.283185307179586 * Math.floor((angle1 + 3.141592653589793 - center1) / 6.283185307179586);
        double actual1 = MathUtils.normalizeAngle(angle1, center1);
        assertEquals(expected1, actual1, 0.0001);
        
        double angle2 = -3.9;
        double center2 = -1.0;
        double expected2 = angle2 - 6.283185307179586 * Math.floor((angle2 + 3.141592653589793 - center2) / 6.283185307179586);
        double actual2 = MathUtils.normalizeAngle(angle2, center2);
        assertEquals(expected2, actual2, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a1 = 10.5;
        double period1 = 4.0;
        double offset1 = 1.0;
        double expected1 = a1 - period1 * Math.floor((a1 - offset1) / period1) - offset1;
        double actual1 = MathUtils.reduce(a1, period1, offset1);
        assertEquals(expected1, actual1, 0.0001);
        
        double a2 = -5.7;
        double period2 = 3.0;
        double offset2 = -2.0;
        double expected2 = a2 - period2 * Math.floor((a2 - offset2) / period2) - offset2;
        double actual2 = MathUtils.reduce(a2, period2, offset2);
        assertEquals(expected2, actual2, 0.0001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude1 = 5;
        byte sign1 = 1;
        byte expected1 = magnitude1;
        byte actual1 = MathUtils.copySign(magnitude1, sign1);
        assertEquals(expected1, actual1);
        
        byte magnitude2 = -5;
        byte sign2 = -1;
        byte expected2 = magnitude2;
        byte actual2 = MathUtils.copySign(magnitude2, sign2);
        assertEquals(expected2, actual2);
        
        byte magnitude3 = -128;
        byte sign3 = 1;
        try {
            MathUtils.copySign(magnitude3, sign3);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude1 = 10;
        short sign1 = 2;
        short expected1 = magnitude1;
        short actual1 = MathUtils.copySign(magnitude1, sign1);
        assertEquals(expected1, actual1);
        
        short magnitude2 = -10;
        short sign2 = -2;
        short expected2 = magnitude2;
        short actual2 = MathUtils.copySign(magnitude2, sign2);
        assertEquals(expected2, actual2);
        
        short magnitude3 = -32768;
        short sign3 = 1;
        try {
            MathUtils.copySign(magnitude3, sign3);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude1 = 100;
        int sign1 = 5;
        int expected1 = magnitude1;
        int actual1 = MathUtils.copySign(magnitude1, sign1);
        assertEquals(expected1, actual1);
        
        int magnitude2 = -100;
        int sign2 = -5;
        int expected2 = magnitude2;
        int actual2 = MathUtils.copySign(magnitude2, sign2);
        assertEquals(expected2, actual2);
        
        int magnitude3 = Integer.MIN_VALUE;
        int sign3 = 1;
        try {
            MathUtils.copySign(magnitude3, sign3);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude1 = 1000L;
        long sign1 = 10L;
        long expected1 = magnitude1;
        long actual1 = MathUtils.copySign(magnitude1, sign1);
        assertEquals(expected1, actual1);
        
        long magnitude2 = -1000L;
        long sign2 = -10L;
        long expected2 = magnitude2;
        long actual2 = MathUtils.copySign(magnitude2, sign2);
        assertEquals(expected2, actual2);
        
        long magnitude3 = Long.MIN_VALUE;
        long sign3 = 1L;
        try {
            MathUtils.copySign(magnitude3, sign3);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        double x1 = 1.23;
        // x1 is finite. Test should pass without throwing any exception.
        MathUtils.checkFinite(x1);
        
        double x2 = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x2);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Test passed.
        }
        
        double x3 = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(x3);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Test passed.
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val1 = {1.0, 2.0, 3.0};
        // val1 are finite. Test should pass without throwing any exception.
        MathUtils.checkFinite(val1);
        
        double[] val2 = {Double.POSITIVE_INFINITY, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val2);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Test passed.
        }
        
        double[] val3 = {1.0, Double.NEGATIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val3);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Test passed.
        }
        
        double[] val4 = {1.0, 2.0, Double.NaN};
        try {
            MathUtils.checkFinite(val4);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Test passed.
        }
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        Object o1 = new Object();
        // o1 is not null. Test should pass without throwing any exception.
        MathUtils.checkNotNull(o1, LocalizedFormats.ARRAY_ELEMENT, new Object[] { 0 });
        
        Object o2 = null;
        try {
            MathUtils.checkNotNull(o2, LocalizedFormats.ARRAY_ELEMENT, new Object[] { 0 });
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Test passed.
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object o1 = new Object();
        // o1 is not null. Test should pass without throwing any exception.
        MathUtils.checkNotNull(o1);
        
        Object o2 = null;
        try {
            MathUtils.checkNotNull(o2);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Test passed.
        }
    }
}