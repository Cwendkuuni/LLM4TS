import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {
    
    @Test
    public void testHashDouble() {
        double value = 10.5;
        assertEquals(MathUtils.hash(value), new Double(value).hashCode());
    }
    
    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        assertEquals(MathUtils.hash(value), Arrays.hashCode(value));
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 5.0;
        assertEquals(MathUtils.normalizeAngle(a, center), a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586));
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        assertEquals(MathUtils.reduce(a, period, offset), a - period * FastMath.floor((a - offset) / period) - offset);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        try {
            assertEquals(MathUtils.copySign(magnitude, sign), -magnitude);
        } catch (MathArithmeticException e) {
            assertEquals(e.getMessage(), LocalizedFormats.OVERFLOW.name());
        }
    }
    
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        try {
            assertEquals(MathUtils.copySign(magnitude, sign), -magnitude);
        } catch (MathArithmeticException e) {
            assertEquals(e.getMessage(), LocalizedFormats.OVERFLOW.name());
        }
    }
    
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        try {
            assertEquals(MathUtils.copySign(magnitude, sign), -magnitude);
        } catch (MathArithmeticException e) {
            assertEquals(e.getMessage(), LocalizedFormats.OVERFLOW.name());
        }
    }
    
    @Test
    public void testCopySignLong() {
        long magnitude = 1000000L;
        long sign = -500000L;
        try {
            assertEquals(MathUtils.copySign(magnitude, sign), -magnitude);
        } catch (MathArithmeticException e) {
            assertEquals(e.getMessage(), LocalizedFormats.OVERFLOW.name());
        }
    }
    
    @Test
    public void testCheckFiniteDouble() {
        double x = 10.5;
        MathUtils.checkFinite(x);
    }
    
    @Test
    public void testCheckFiniteDoubleArray() {
        double[] val = {1.0, 2.0, Double.POSITIVE_INFINITY};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            assertEquals(e.getMessage(), LocalizedFormats.ARRAY_ELEMENT.name());
        }
    }
    
    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.UNKNOWN_FORMAT;
        try {
            MathUtils.checkNotNull(o, pattern);
        } catch (NullArgumentException e) {
            assertEquals(e.getMessage(), LocalizedFormats.UNKNOWN_FORMAT.name());
        }
    }
    
    @Test
    public void testCheckNotNullWithoutPattern() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            assertEquals(e.getMessage(), "null");
        }
    }
}