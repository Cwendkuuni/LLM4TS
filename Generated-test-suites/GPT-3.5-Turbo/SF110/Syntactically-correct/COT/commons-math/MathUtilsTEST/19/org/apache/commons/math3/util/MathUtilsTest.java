import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    
    @Test
    public void testHash() {
        double value = 5.0;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 0.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testCopySignByte_positivePositive() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = 3;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignByte_positiveNegative() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = -3;
        byte expected = (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignByte_negativePositive_overflow() {
        byte magnitude = -128;
        byte sign = 5;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignShort_positivePositive() throws MathArithmeticException {
        short magnitude = 5;
        short sign = 3;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignShort_positiveNegative() throws MathArithmeticException {
        short magnitude = 5;
        short sign = -3;
        short expected = (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignShort_negativePositive_overflow() {
        short magnitude = -32768;
        short sign = 5;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignInt_positivePositive() throws MathArithmeticException {
        int magnitude = 5;
        int sign = 3;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignInt_positiveNegative() throws MathArithmeticException {
        int magnitude = 5;
        int sign = -3;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignInt_negativePositive_overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 5;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCopySignLong_positivePositive() throws MathArithmeticException {
        long magnitude = 5L;
        long sign = 3L;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignLong_positiveNegative() throws MathArithmeticException {
        long magnitude = 5L;
        long sign = -3L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySignLong_negativePositive_overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 5L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals(LocalizedFormats.OVERFLOW, e.getPattern());
        }
    }
    
    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        double x = 5.0;
        MathUtils.checkFinite(x);
        // If no exception is thrown, the method is considered to pass
    }
    
    @Test
    public void testCheckFinite_Infinite() {
        double x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }
    
    @Test
    public void testCheckFinite_NaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }
    
    @Test
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
        // If no exception is thrown, the method is considered to pass
    }
    
    @Test
    public void testCheckFiniteArray_Infinite() {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }
    
    @Test
    public void testCheckFiniteArray_NaN() {
        double[] val = {1.0, Double.NaN, 3.0};
        try {
            MathUtils.checkFinite(val);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }
    
    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        // If no exception is thrown, the method is considered to pass
    }
    
    @Test
    public void testCheckNotNull_NullPattern() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }
    
    @Test
    public void testCheckNotNull_NullArg() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o, Localizable.DEFAULT);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // If the expected exception is thrown, the method is considered to pass
        }
    }

}