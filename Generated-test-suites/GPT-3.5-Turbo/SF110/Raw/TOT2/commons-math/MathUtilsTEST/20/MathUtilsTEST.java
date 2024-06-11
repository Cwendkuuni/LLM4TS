import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 3.14;
        assertEquals(MathUtils.hash(value), Double.valueOf(value).hashCode());
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {3.14, 2.71};
        assertEquals(MathUtils.hash(value), Arrays.hashCode(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 5.0;
        double center = 2.0;
        assertEquals(MathUtils.normalizeAngle(a, center), 3.141592653589793);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        assertEquals(MathUtils.reduce(a, period, offset), 1.0);
    }

    @Test
    public void testCopySign_byte() {
        byte magnitude = 1;
        byte sign = -1;
        assertEquals(MathUtils.copySign(magnitude, sign), (byte) -1);
    }

    @Test
    public void testCheckFinite_double() {
        double x = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(x));
    }

    @Test
    public void testCheckNotNull() {
        Object o = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o));
    }

}