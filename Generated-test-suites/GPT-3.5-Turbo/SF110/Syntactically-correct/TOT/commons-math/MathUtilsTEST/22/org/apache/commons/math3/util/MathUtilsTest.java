import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        // Typical use case
        double value1 = 3.14;
        int expected1 = new Double(value1).hashCode();
        assertEquals(expected1, MathUtils.hash(value1));
        
        // Edge case
        double value2 = 0.0;
        int expected2 = new Double(value2).hashCode();
        assertEquals(expected2, MathUtils.hash(value2));
    }
    
    @Test
    public void testHash_doubleArray() {
        // Typical use case
        double[] value1 = {1.0, 2.0, 3.0};
        int expected1 = value1.hashCode();
        assertEquals(expected1, MathUtils.hash(value1));
        
        // Edge case
        double[] value2 = {};
        int expected2 = value2.hashCode();
        assertEquals(expected2, MathUtils.hash(value2));        
    }
    
    @Test
    public void testNormalizeAngle() {
        // Typical use case
        double a1 = 8.5;
        double center1 = 3.0;
        double expected1 = a1 - MathUtils.TWO_PI * FastMath.floor((a1 + 3.141592653589793 - center1) / MathUtils.TWO_PI);
        assertEquals(expected1, MathUtils.normalizeAngle(a1, center1), 0.00001);
        
        // Edge case
        double a2 = 0.0;
        double center2 = 0.0;
        double expected2 = a2 - MathUtils.TWO_PI * FastMath.floor((a2 + 3.141592653589793 - center2) / MathUtils.TWO_PI);
        assertEquals(expected2, MathUtils.normalizeAngle(a2, center2), 0.00001);
    }
    
    @Test
    public void testReduce() {
        // Typical use case
        double a1 = 15.0;
        double period1 = 10.0;
        double offset1 = 5.0;
        double expected1 = a1 - period1 * FastMath.floor((a1 - offset1) / period1) - offset1;
        assertEquals(expected1, MathUtils.reduce(a1, period1, offset1), 0.00001);
        
        // Edge case
        double a2 = 7.0;
        double period2 = 0.0;
        double offset2 = 0.0;
        double expected2 = a2 - period2 * FastMath.floor((a2 - offset2) / period2) - offset2;
        assertEquals(expected2, MathUtils.reduce(a2, period2, offset2), 0.00001);
    }
    
    @Test
    public void testCopySign_byte() {
        // Typical use case
        byte magnitude1 = 5;
        byte sign1 = -1;
        byte expected1 = magnitude1;
        assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
        
        // Edge case
        byte magnitude2 = Byte.MAX_VALUE;
        byte sign2 = -1;
        try {
            MathUtils.copySign(magnitude2, sign2);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySign_short() {
        // Typical use case
        short magnitude1 = 50;
        short sign1 = -1;
        short expected1 = magnitude1;
        assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
        
        // Edge case
        short magnitude2 = Short.MAX_VALUE;
        short sign2 = -1;
        try {
            MathUtils.copySign(magnitude2, sign2);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySign_int() {
        // Typical use case
        int magnitude1 = 1000;
        int sign1 = -1;
        int expected1 = magnitude1;
        assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
        
        // Edge case
        int magnitude2 = Integer.MIN_VALUE;
        int sign2 = -1;
        try {
            MathUtils.copySign(magnitude2, sign2);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCopySign_long() {
        // Typical use case
        long magnitude1 = 1000000L;
        long sign1 = -1L;
        long expected1 = magnitude1;
        assertEquals(expected1, MathUtils.copySign(magnitude1, sign1));
        
        // Edge case
        long magnitude2 = Long.MIN_VALUE;
        long sign2 = -1L;
        try {
            MathUtils.copySign(magnitude2, sign2);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        // Typical use case
        double x1 = 10.5;
        MathUtils.checkFinite(x1);
        
        // Error scenario
        double x2 = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x2);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        // Typical use case
        double[] val1 = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val1);
        
        // Error scenario
        double[] val2 = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val2);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNull_Localizable_ObjectArray() {
        // Typical use case
        Object o1 = new Object();
        Localizable pattern1 = new LocalizedFormats();
        Object[] args1 = {1, 2, 3};
        MathUtils.checkNotNull(o1, pattern1, args1);
        
        // Error scenario
        Object o2 = null;
        Localizable pattern2 = new LocalizedFormats();
        Object[] args2 = {1, 2, 3};
        try {
            MathUtils.checkNotNull(o2, pattern2, args2);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        // Typical use case
        Object o1 = new Object();
        MathUtils.checkNotNull(o1);
        
        // Error scenario
        Object o2 = null;
        try {
            MathUtils.checkNotNull(o2);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Exception expected
        }
    }
}