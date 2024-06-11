import org.apache.commons.math3.exception.*;
import org.junit.Test;
import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashDoubleArray() {
        double[] value = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 5.5;
        double center = 2.2;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testReduce() {
        double a = 4.6;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.00001);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte)(-magnitude);
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.OVERFLOW.toString());
        }
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = (short)(-magnitude);
        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.OVERFLOW.toString());
        }
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = -magnitude;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.OVERFLOW.toString());
        }
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = 1000000L;
        long sign = -500000L;
        long expected = -magnitude;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.OVERFLOW.toString());
        }
    }
    
    @Test
    public void testCheckFiniteDouble() {
        double x = 3.14;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.NOT_FINITE_NUMBER.toString());
        }
    }
    
    @Test
    public void testCheckFiniteDoubleArray() {
        double[] val = {1.2, 2.3, Double.POSITIVE_INFINITY};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), LocalizedFormats.ARRAY_ELEMENT.toString());
        }
    }
    
    @Test
    public void testCheckNotNull() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            // Verify if the exception is thrown correctly
            assertNull(e.getLocalizedMessage());
        }
    }
    
    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.INVALID_ARGUMENT;
        Object[] args = { "o" };
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            // Verify if the exception is thrown correctly
            assertEquals(e.getLocalizedMessage(), "Invalid argument: o");
        }
    }

}