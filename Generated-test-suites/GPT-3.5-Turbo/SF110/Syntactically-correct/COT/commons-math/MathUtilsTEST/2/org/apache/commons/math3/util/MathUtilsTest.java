
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 1.234;
        int result = MathUtils.hash(value);
        assertEquals(Double.valueOf(value).hashCode(), result);
    }

    @Test
    public void testHashArray() {
        double[] value = {1.234, 5.678};
        int result = MathUtils.hash(value);
        assertEquals(Arrays.hashCode(value), result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 7.0;
        double center = 2.0;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(0.0, result, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(1.0, result, 0.000001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -1;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(-5, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(-100, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(-1000, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 100000;
        long sign = -1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(-100000, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() {
        double x = 1.234;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.234, 5.678};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] val = {1.234, Double.NaN, 5.678};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInfinity() {
        double[] val = {1.234, Double.POSITIVE_INFINITY, 5.678};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.OVERFLOW, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}
