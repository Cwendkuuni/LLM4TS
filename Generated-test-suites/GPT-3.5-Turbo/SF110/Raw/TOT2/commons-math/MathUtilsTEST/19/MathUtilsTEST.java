import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash_Double() {
        assertEquals(123456, MathUtils.hash(123.456));
    }

    @Test
    public void testHash_DoubleArray() {
        double[] array = {1.0, 2.0, 3.0};
        assertEquals(123, MathUtils.hash(array));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5, MathUtils.normalizeAngle(7.5, 6.0), 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(2.0, MathUtils.reduce(8.0, 3.0, 1.0), 0.0);
    }

    @Test
    public void testCopySign_Byte() {
        assertEquals(10, MathUtils.copySign((byte) 10, (byte) -5));
    }

    @Test
    public void testCopySign_Short() {
        assertEquals(20, MathUtils.copySign((short) 20, (short) -10));
    }

    @Test
    public void testCopySign_Int() {
        assertEquals(30, MathUtils.copySign(30, -15));
    }

    @Test
    public void testCopySign_Long() {
        assertEquals(40L, MathUtils.copySign(40L, -20L));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_NumberInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite_NumberNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_ObjectNull() {
        MathUtils.checkNotNull(null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNull_ObjectWithPatternNull() {
        MathUtils.checkNotNull(null, null);
    }

}