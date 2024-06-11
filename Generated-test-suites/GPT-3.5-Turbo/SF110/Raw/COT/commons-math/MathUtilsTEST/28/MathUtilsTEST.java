import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14159;
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
        double a = 7.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testReduce() {
        double a = 7.0;
        double period = 4.0;
        double offset = 2.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 8;
        byte sign = -4;
        byte expected = (byte)(-magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 8;
        short sign = -4;
        short expected = (short)(-magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 8;
        int sign = -4;
        int expected = -magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 8L;
        long sign = -4L;
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
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
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