import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {
    @Test
    public void testHash1() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHash2() {
        double[] value = {10.5, 20.8, 30.3};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 2.5;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.5;
        double offset = 5.9;
        double p = FastMath.abs(period);
        double expected = a - p * FastMath.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.00001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (byte)(-magnitude);
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 500;
        short sign = -300;
        short expected = (short)(-magnitude);
        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 10000;
        int sign = -5000;
        int expected = -magnitude;
        try {
            int result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("MathArithmeticException thrown");
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000000000L;
        long sign = -5000000000L;
        long expected = -magnitude;
        try {
            long result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("MathArithmeticException thrown");
        }
    }

    @Test
    public void testCheckFinite1() {
        double x = 10.5;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException thrown");
        }
    }

    @Test
    public void testCheckFinite2() {
        double[] val = {10.5, 20.8, 30.3};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException thrown");
        }
    }

    @Test
    public void testCheckNotNull1() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("NullArgumentException thrown");
        }
    }

    @Test
    public void testCheckNotNull2() {
        Object o = null;
        try {
            MathUtils.checkNotNull(o);
            fail("Expected NullArgumentException not thrown");
        } catch (NullArgumentException e) {
            // Expected exception thrown
        }
    }
}