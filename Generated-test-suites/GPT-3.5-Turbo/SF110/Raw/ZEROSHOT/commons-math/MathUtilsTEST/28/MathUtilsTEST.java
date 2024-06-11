import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 1.0;
        int expected = -1022361332;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = -1258167214;
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        double expected = 4.716814692820413;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testReduce() {
        double a = 7.0;
        double period = 3.0;
        double offset = 1.0;
        double expected = 1.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 1;
        byte sign = -1;
        byte expected = -1;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 1;
        short sign = -1;
        short expected = -1;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = 1;
        int sign = -1;
        int expected = -1;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = 1L;
        long sign = -1L;
        long expected = -1L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        double x = 1.0;
        MathUtils.checkFinite(x);
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
    public void testCheckFiniteArray() throws NotFiniteNumberException {
        double[] val = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayElement() throws NotFiniteNumberException {
        double[] val = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        Object o = null;
        MathUtils.checkNotNull(o, null);
    }
}