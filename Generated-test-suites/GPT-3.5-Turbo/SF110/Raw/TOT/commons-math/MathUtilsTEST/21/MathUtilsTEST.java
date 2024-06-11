import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        // Typical use case
        double value = 10.5;
        assertEquals(Double.valueOf(value).hashCode(), MathUtils.hash(value));
    }

    @Test
    public void testHash_doubleArray() {
        // Typical use case
        double[] value = {1.2, 3.4, 5.6};
        assertEquals(Arrays.hashCode(value), MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        // Typical use case
        double a = 7.2;
        double center = 2.0;
        double expected = a - 2 * MathUtils.TWO_PI;
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.0001);

        // Edge case
        a = 10.0;
        center = 3.0;
        expected = 6.283185307179586 * (2 * MathUtils.TWO_PI - 0.141592653589793);
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.0001);
    }

    @Test
    public void testReduce() {
        // Typical use case
        double a = 5.5;
        double period = 3.0;
        double offset = 1.0;
        double expected = a - period;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.0001);

        // Edge case
        a = 10.0;
        period = 0.0;
        offset = 5.0;
        expected = a - period - offset;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.0001);
    }

    @Test
    public void testCopySign_byte() {
        // Typical use case
        byte magnitude = 5;
        byte sign = -2;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));

        // Edge case
        magnitude = 127;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // success
        }
    }

    @Test
    public void testCopySign_short() {
        // Typical use case
        short magnitude = 10;
        short sign = -5;
        assertEquals(-10, MathUtils.copySign(magnitude, sign));

        // Edge case
        magnitude = 32767;
        sign = 0;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // success
        }
    }

    @Test
    public void testCopySign_int() {
        // Typical use case
        int magnitude = 7;
        int sign = -3;
        assertEquals(-7, MathUtils.copySign(magnitude, sign));

        // Edge case
        magnitude = Integer.MAX_VALUE;
        sign = 0;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // success
        }
    }

    @Test
    public void testCopySign_long() {
        // Typical use case
        long magnitude = 15L;
        long sign = -8L;
        assertEquals(-15L, MathUtils.copySign(magnitude, sign));

        // Edge case
        magnitude = Long.MAX_VALUE;
        sign = 0L;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // success
        }
    }

    @Test
    public void testCheckFinite_double() {
        // Typical use case
        double x = 10.5;
        MathUtils.checkFinite(x); // no exception expected

        // Error scenario
        x = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(x);
            fail();
        } catch (NotFiniteNumberException e) {
            // success
            assertEquals(x, e.getArgument());
        }
    }

    @Test
    public void testCheckFinite_doubleArray() {
        // Typical use case
        double[] val = {1.2, 3.4, 5.6};
        MathUtils.checkFinite(val); // no exception expected

        // Error scenario
        val[1] = Double.NaN;
        try {
            MathUtils.checkFinite(val);
            fail();
        } catch (NotFiniteNumberException e) {
            // success
            assertEquals(val[1], e.getArgument());
            assertEquals(1, e.getContext());
        }
    }

    @Test
    public void testCheckNotNull_Localizable() {
        // Typical use case
        Object o = new Object();
        MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 2); // no exception expected

        // Error scenario
        o = null;
        try {
            MathUtils.checkNotNull(o, LocalizedFormats.ARGUMENTS_NULL_ARRAY);
            fail();
        } catch (NullArgumentException e) {
            // success
        }
    }

    @Test
    public void testCheckNotNull_Object() {
        // Typical use case
        Object o = new Object();
        MathUtils.checkNotNull(o); // no exception expected

        // Error scenario
        o = null;
        try {
            MathUtils.checkNotNull(o);
            fail();
        } catch (NullArgumentException e) {
            // success
        }
    }

}