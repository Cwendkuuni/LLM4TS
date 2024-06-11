import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 5.0;
        assertEquals(Double.hashCode(value), MathUtils.hash(value));
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        assertEquals(Arrays.hashCode(value), MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 1.0;
        assertEquals(3.858407346410208, MathUtils.normalizeAngle(a, center), 0.0000001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 1.0;
        assertEquals(1.0, MathUtils.reduce(a, period, offset), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals(5, MathUtils.copySign((byte)5, (byte)10));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte)-128, (byte)10));
    }

    @Test
    public void testCopySignShort() {
        assertEquals(5, MathUtils.copySign((short)5, (short)10));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short)-32768, (short)10));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(5, MathUtils.copySign(5, 10));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Integer.MIN_VALUE, 10));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(5, MathUtils.copySign(5L, 10L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Long.MIN_VALUE, 10L));
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = 10.0;
        MathUtils.checkFinite(x);
        assertFalse(Double.isInfinite(x) || Double.isNaN(x));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] val = {1.0, 2.0, Double.NaN};
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(val));
    }

    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o, LocalizedFormats.NULL_NOT_ALLOWED, "param"));
    }

    @Test
    public void testCheckNotNullWithoutPattern() {
        Object o = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o));
    }
}