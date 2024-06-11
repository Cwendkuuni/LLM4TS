import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(1072694096, MathUtils.hash(2.5));
    }

    @Test
    public void testHashDoubleArray() {
        assertEquals(1069547520, MathUtils.hash(new double[] {2.5, 3.0}));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.7853981633974483, MathUtils.normalizeAngle(4.0, 3.0), 0.0000001);
    }

    @Test
    public void testReduce() {
        assertEquals(3.0, MathUtils.reduce(10.0, 7.0, 2.0), 0.0000001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals(-5, MathUtils.copySign((byte)5, (byte)-1));
        try {
            MathUtils.copySign((byte)-128, (byte)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignShort() {
        assertEquals(-10, MathUtils.copySign((short)10, (short)-1));
        try {
            MathUtils.copySign((short)-32768, (short)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignInt() {
        assertEquals(-20, MathUtils.copySign(20, -1));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignLong() {
        assertEquals(-30L, MathUtils.copySign(30L, -1L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(7.5);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        MathUtils.checkFinite(new double[] {2.0, 3.5, Double.NaN});
        try {
            MathUtils.checkFinite(new double[] {1.0, Double.NEGATIVE_INFINITY, 5.0});
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullWithPattern() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.NO_ARGUMENT);
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.NO_ARGUMENT);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullWithoutPattern() {
        MathUtils.checkNotNull(new Object());
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }
}