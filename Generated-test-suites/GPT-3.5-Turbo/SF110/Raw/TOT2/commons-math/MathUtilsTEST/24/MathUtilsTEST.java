import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        int expectedHash = new Double(value).hashCode();
        assertEquals(expectedHash, MathUtils.hash(value));
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expectedHash = Arrays.hashCode(value);
        assertEquals(expectedHash, MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 5.0;
        double expectedNormalizedAngle = -1.7168146928204138;
        assertEquals(expectedNormalizedAngle, MathUtils.normalizeAngle(a, center), 0.0000001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        double expectedReducedValue = 1.0;
        assertEquals(expectedReducedValue, MathUtils.reduce(a, period, offset), 0.0000001);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expectedValue = -10;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_short() {
        short magnitude = 100;
        short sign = -50;
        short expectedValue = -100;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_int() {
        int magnitude = 1000;
        int sign = -500;
        int expectedValue = -1000;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySign_long() {
        long magnitude = 1000000000L;
        long sign = -500000000L;
        long expectedValue = -1000000000L;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
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
    public void testCheckNotNull_withPattern() {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.NUMBER_TOO_SMALL, 0, 1);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_withoutPattern() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}