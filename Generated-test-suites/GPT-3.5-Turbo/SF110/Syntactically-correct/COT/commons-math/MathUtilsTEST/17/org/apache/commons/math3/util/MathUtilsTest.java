import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 3.14;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.0;
        double center = 1.0;
        double expected = a - MathUtils.TWO_PI * FastMath.floor((a + 3.141592653589793 - center) / MathUtils.TWO_PI);
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 5.0;
        double period = 2.0;
        double offset = 1.0;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = 1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = 1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 100000;
        long sign = -1;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFinite() {
        double x = 5.0;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinite() {
        double[] values = {1.0, Double.NEGATIVE_INFINITY, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] values = {1.0, 2.0, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithoutPattern() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}