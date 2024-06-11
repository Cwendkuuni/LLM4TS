import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 15.6;
        int expected = new Double(value).hashCode();
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.2, 3.4, 5.6};
        int expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.6;
        double center = 3.8;
        double expected = a - MathUtils.TWO_PI * Math.floor((a + Math.PI - center) / MathUtils.TWO_PI);
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.000001);
    }

    @Test
    public void testReduce() {
        double a = -17.3;
        double period = 10.0;
        double offset = 5.0;
        double expected = a - period * Math.floor((a - offset) / period) - offset;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.000001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        exception.expect(MathArithmeticException.class);
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        exception.expect(MathArithmeticException.class);
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 200;
        int sign = -100;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        exception.expect(MathArithmeticException.class);
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 500L;
        long sign = -250L;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        exception.expect(MathArithmeticException.class);
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() {
        double x = 15.6;
        MathUtils.checkFinite(x);
        assertTrue(true);
    }

    @Test
    public void testCheckFiniteNan() {
        double x = Double.NaN;
        exception.expect(NotFiniteNumberException.class);
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteInfinite() {
        double x = Double.POSITIVE_INFINITY;
        exception.expect(NotFiniteNumberException.class);
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
        assertTrue(true);
    }

    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.UNKNOWN_FORMAT;
        exception.expect(NullArgumentException.class);
        MathUtils.checkNotNull(o, pattern);
    }

}