import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
        
        value = Double.POSITIVE_INFINITY;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
        
        value = Double.NEGATIVE_INFINITY;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
        
        value = Double.NaN;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        assertEquals(Arrays.hashCode(value), MathUtils.hash(value));
        
        double[] empty = {};
        assertEquals(Arrays.hashCode(empty), MathUtils.hash(empty));
    }
    
    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(7.0, 2.0), 0.000001);
        assertEquals(2.0, MathUtils.normalizeAngle(8.0, 3.0), 0.000001);
        assertEquals(3.0, MathUtils.normalizeAngle(9.0, 4.0), 0.000001);
    }
    
    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(5.0, 4.0, 2.0), 0.000001);
        assertEquals(2.0, MathUtils.reduce(6.0, 5.0, 3.0), 0.000001);
        assertEquals(3.0, MathUtils.reduce(7.0, 6.0, 4.0), 0.000001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = 5;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -10;
        sign = -5;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -128;
        sign = 5;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = 50;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -100;
        sign = -50;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -32768;
        sign = 100;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = 500;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -1000;
        sign = -500;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = Integer.MIN_VALUE;
        sign = 1000;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 1000000L;
        long sign = 500000L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = -1000000L;
        sign = -500000L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
        
        magnitude = Long.MIN_VALUE;
        sign = 1000000L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException to be thrown");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getSpecifier());
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 10.5;
        MathUtils.checkFinite(x);
        
        x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // pass
        }
        
        x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            // pass
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
        
        double[] inf = {Double.POSITIVE_INFINITY, 2.0, 3.0};
        try {
            MathUtils.checkFinite(inf);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecifier());
            assertEquals(0, e.getFormatArguments()[0]);
        }
        
        double[] nan = {1.0, 2.0, Double.NaN};
        try {
            MathUtils.checkFinite(nan);
            fail("Expected NotFiniteNumberException to be thrown");
        } catch (NotFiniteNumberException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecifier());
            assertEquals(2, e.getFormatArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        Object o = new Object();
        MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 1);
        
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            assertEquals(LocalizedFormats.ARRAY_ELEMENT, e.getSpecifier());
            assertEquals(1, e.getFormatArguments()[0]);
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // pass
        }
    }
}