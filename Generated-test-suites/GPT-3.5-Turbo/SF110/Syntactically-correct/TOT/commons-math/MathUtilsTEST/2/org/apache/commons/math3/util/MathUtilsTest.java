import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHash() {
        double value = 3.14;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHashArray() {
        double[] value = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.7;
        double center = 2.1;
        double expected = a - (6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586));
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.5;
        double offset = 1.5;
        double p = Math.abs(period);
        double expected = a - (p * Math.floor((a - offset) / p)) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.000001);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 2;
        byte sign = -3;
        byte expected = (byte)-magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 2000;
        short sign = -3000;
        short expected = (short)-magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = 100000;
        int sign = -200000;
        int expected = -magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = 8000000000L;
        long sign = -9000000000L;
        long expected = -magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        double x = 1.5;
        MathUtils.checkFinite(x);
        // No exception thrown, test passed
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
        double[] val = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(val);
        // No exception thrown, test passed
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayHasInfinity() throws NotFiniteNumberException {
        double[] val = {1.2, 2.3, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayHasNaN() throws NotFiniteNumberException {
        double[] val = {1.2, 2.3, Double.NaN};
        MathUtils.checkFinite(val);
    }

    @Test
    public void testCheckNotNullWithPattern() throws NullArgumentException {
        Object obj = new Object();
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(obj, pattern, args);
        // No exception thrown, test passed
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithPatternNull() throws NullArgumentException {
        Object obj = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(obj, pattern, args);
    }

    @Test
    public void testCheckNotNull() throws NullArgumentException {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
        // No exception thrown, test passed
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() throws NullArgumentException {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
}