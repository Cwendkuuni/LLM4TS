import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        assertEquals(1627736093, MathUtils.hash(3.14));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(-1078530014, MathUtils.hash(-2.718));
    }

    @Test
    public void testHash_doubleArray() {
        double[] arr1 = {1.0, 2.0, 3.0};
        double[] arr2 = {4.0, 5.0, 6.0};

        assertEquals(-127787479, MathUtils.hash(arr1));
        assertEquals(3037976, MathUtils.hash(arr2));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(7.853981633974483, 3.141592653589793), 0.000001);
        assertEquals(0.0, MathUtils.normalizeAngle(6.283185307179586, 3.141592653589793), 0.000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-7.853981633974483, -3.141592653589793), 0.000001);
    }

    @Test
    public void testReduce() {
        assertEquals(3.0, MathUtils.reduce(16.0, 5.0, 8.0), 0.000001);
        assertEquals(1.0, MathUtils.reduce(11.0, -5.0, 8.0), 0.000001);
        assertEquals(-2.5, MathUtils.reduce(-5.5, -2.0, -3.0), 0.000001);
    }

    @Test
    public void testCopySign_byte() {
        try {
            assertEquals(-25, MathUtils.copySign((byte) 25, (byte) -1));
            assertEquals(25, MathUtils.copySign((byte) -25, (byte) -1));
            assertEquals(-128, MathUtils.copySign((byte) 128, (byte) -1));
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_short() {
        try {
            assertEquals(-25000, MathUtils.copySign((short) 25000, (short) -1));
            assertEquals(25000, MathUtils.copySign((short) -25000, (short) -1));
            assertEquals(-32768, MathUtils.copySign((short) 32768, (short) -1));
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_int() {
        try {
            assertEquals(-250000, MathUtils.copySign(250000, -1));
            assertEquals(250000, MathUtils.copySign(-250000, -1));
            assertEquals(2147483647, MathUtils.copySign(Integer.MAX_VALUE, -1));
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testCopySign_long() {
        try {
            assertEquals(-25000000000L, MathUtils.copySign(25000000000L, -1L));
            assertEquals(25000000000L, MathUtils.copySign(-25000000000L, -1L));
            assertEquals(-9223372036854775808L, MathUtils.copySign(Long.MAX_VALUE, -1L));
        } catch (MathArithmeticException e) {
            fail("Unexpected exception");
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_double() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_doubleArray() {
        double[] arr = {1.0, Double.NaN, 2.0};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_pattern() {
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_object() {
        MathUtils.checkNotNull(null);
    }
}