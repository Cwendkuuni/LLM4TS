import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 5.2;
        double center = 1.5;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 4.0;
        double offset = 1.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -1;
        try {
            byte expected = (byte)(-magnitude);
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -1;
        try {
            short expected = (short)(-magnitude);
            short actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -1;
        try {
            int expected = -magnitude;
            int actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 1000000000L;
        long sign = -1L;
        try {
            long expected = -magnitude;
            long actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_double() {
        double x = 1.234;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Object_Localizable_ObjectArray() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckNotNull_Object() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}