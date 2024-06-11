import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 5.0;
        double expected = -3.141592653589793;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 4.0;
        double offset = 2.0;
        double expected = 0.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = -10;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = -100;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = -1000;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000L;
        long sign = -5000L;
        long expected = -10000L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFinite() {
        double x = 10.0;
        MathUtils.checkFinite(x);
        // No exception expected
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteException() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        // No exception expected
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullException() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

}