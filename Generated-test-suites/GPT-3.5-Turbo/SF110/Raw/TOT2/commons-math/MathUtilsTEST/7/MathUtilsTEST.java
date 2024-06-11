import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14;
        int expected = Double.valueOf(value).hashCode();
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {2.5, 5.0};
        int expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10;
        double center = 2;
        double expected = 2;
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.01);
    }

    @Test
    public void testReduce() {
        double a = 15;
        double period = 5;
        double offset = 3;
        double expected = 2;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.01);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 3;
        byte sign = -5;
        byte expected = 3;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 10;
        short sign = -15;
        short expected = 10;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 100;
        int sign = -150;
        int expected = 100;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 1000;
        long sign = -1500;
        long expected = 1000;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFinite_double() {
        double x = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(x));
    }

    @Test
    public void testCheckFinite_doubleArray() {
        double[] val = {1.0, Double.NaN};
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(val));
    }

    @Test
    public void testCheckNotNull() {
        Object o = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o));
    }
}