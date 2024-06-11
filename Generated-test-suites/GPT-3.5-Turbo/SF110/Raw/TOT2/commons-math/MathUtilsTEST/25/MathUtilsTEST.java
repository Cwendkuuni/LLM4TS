import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123456, MathUtils.hash(123.456));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.57, MathUtils.normalizeAngle(4.71, 3.14), 0.01);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        MathUtils.copySign((byte)127, (byte)-1);
    }

    @Test
    public void testHashDoubleArray() {
        double[] arr = {1.0, 2.0, 3.0};
        assertEquals(-1246697977, MathUtils.hash(arr));
    }

    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(10.0, 9.0, 4.0), 0.01);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        MathUtils.copySign(Long.MAX_VALUE, 1L);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull() {
        MathUtils.checkNotNull(null);
    }
}