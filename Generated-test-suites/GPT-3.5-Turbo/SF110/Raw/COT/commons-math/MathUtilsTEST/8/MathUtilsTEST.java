import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MathUtilsTest {

    // Test for hash(double value)
    @Test
    public void testHash() {
        double value = 2.5;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    // Test for hash(double[] value)
    @Test
    public void testHashArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    // Test for normalizeAngle(double a, double center)
    @Test
    public void testNormalizeAngle() {
        double a = 4.5;
        double center = 0.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.0);
    }

    // Test for reduce(double a, double period, double offset)
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.0;
        double offset = 1.0;
        double p = Math.abs(period);
        double expected = a - p * Math.floor((a - offset) / p) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.0);
    }

    // Test for copySign(byte magnitude, byte sign)
    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (byte)(-magnitude);
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    // Test for copySign(short magnitude, short sign)
    @Test
    public void testCopySignShort() {
        short magnitude = 100;
        short sign = -50;
        short expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : (short)(-magnitude);
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    // Test for copySign(int magnitude, int sign)
    @Test
    public void testCopySignInt() {
        int magnitude = 1000;
        int sign = -500;
        int expected = (magnitude >= 0 && sign >= 0) || (magnitude < 0 && sign < 0) ? magnitude : -magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    // Test for copySign(long magnitude, long sign)
    @Test
    public void testCopySignLong() {
        long magnitude = 1000000L;
        long sign = -500000L;
        long expected = (magnitude >= 0L && sign >= 0L) || (magnitude < 0L && sign < 0L) ? magnitude : -magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, result);
    }

    // Test for checkFinite(double x)
    @Test
    public void testCheckFinite() {
        double x = 5.0;
        try {
            MathUtils.checkFinite(x);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException thrown");
        }
    }

    // Test for checkFinite(double[] val)
    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, 2.0, 3.0};
        try {
            MathUtils.checkFinite(val);
        } catch (NotFiniteNumberException e) {
            fail("NotFiniteNumberException thrown");
        }
    }

    // Test for checkNotNull(Object o, Localizable pattern, Object... args)
    @Test
    public void testCheckNotNullWithPattern() {
        Object o = new Object();
        Localizable pattern = LocalizedFormats.OVERFLOW;
        Object[] args = {};
        try {
            MathUtils.checkNotNull(o, pattern, args);
        } catch (NullArgumentException e) {
            fail("NullArgumentException thrown");
        }
    }

    // Test for checkNotNull(Object o)
    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("NullArgumentException thrown");
        }
    }
}