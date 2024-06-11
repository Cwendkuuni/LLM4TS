import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashArray() {
        double[] value = {1.0, 2.5, 3.8};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.7;
        double center = 1.5;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte) (sign < 0 ? -magnitude : magnitude);
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 10;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = (short) (sign < 0 ? -magnitude : magnitude);
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 50;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = (sign < 0 ? -magnitude : magnitude);
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 500;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 1000000L;
        long sign = -500000L;
        long expected = (sign < 0 ? -magnitude : magnitude);
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 500000L;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() {
        double x = 10.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException should not be thrown");
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() throws NotFiniteNumberException {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() throws NotFiniteNumberException {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.5, 3.8};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException should not be thrown");
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() throws NotFiniteNumberException {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.8};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() throws NotFiniteNumberException {
        double[] val = {1.0, Double.NaN, 3.8};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNullWithPattern() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 1);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPatternNull() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 1);
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not be thrown");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}