import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.*;

public class MathUtilsTest {
    
    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.5, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 3.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 4.0;
        double offset = 1.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_short() {
        short magnitude = 300;
        short sign = -100;
        short expected = (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_int() {
        int magnitude = 500;
        int sign = -200;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCopySign_long() {
        long magnitude = 1000L;
        long sign = -500L;
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }
    
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(val);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object_Localizable() {
        Object o = null;
        Localizable pattern = new DummyLocalizable();
        MathUtils.checkNotNull(o, pattern);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_Object() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}

class DummyLocalizable implements Localizable {
    public String toString() {
        return "Dummy";
    }
}