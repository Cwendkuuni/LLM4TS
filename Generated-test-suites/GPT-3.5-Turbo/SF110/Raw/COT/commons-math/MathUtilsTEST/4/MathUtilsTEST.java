import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class MathUtilsTest {

    @Test
    public void testHash() {
        assertEquals(123456, MathUtils.hash(123456.0));
        assertEquals(-654321, MathUtils.hash(-654321.0));
    }

    @Test
    public void testHashArray() {
        double[] array1 = {1.0, 2.0, 3.0};
        double[] array2 = {1.0, 2.0, 3.0};
        double[] array3 = {2.0, 3.0, 1.0};

        assertEquals(array1.hashCode(), MathUtils.hash(array1));
        assertEquals(array2.hashCode(), MathUtils.hash(array2));
        assertEquals(array3.hashCode(), MathUtils.hash(array3));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(MathUtils.TWO_PI, 0.0), 0.0001);
        assertEquals(Math.PI, MathUtils.normalizeAngle(4 * Math.PI, Math.PI), 0.0001);
        assertEquals(Math.PI/2, MathUtils.normalizeAngle(Math.PI + Math.PI/2, Math.PI), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.0, MathUtils.reduce(9.0, 8.0, 1.0), 0.0001);
        assertEquals(-3.0, MathUtils.reduce(-11.5, 8.5, -3.0), 0.0001);
        assertEquals(0.5, MathUtils.reduce(15.0, 8.5, -3.5), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 1));
        assertEquals((byte) -3, MathUtils.copySign((byte) 3, (byte) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) -128, (byte) 1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) 10, MathUtils.copySign((short) 10, (short) 1));
        assertEquals((short) -6, MathUtils.copySign((short) 6, (short) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) -32768, (short) 1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(100, MathUtils.copySign(100, 1));
        assertEquals(-50, MathUtils.copySign(50, -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(10000000000L, MathUtils.copySign(10000000000L, 1L));
        assertEquals(-5000000000L, MathUtils.copySign(5000000000L, -1L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testCheckFinite() {
        MathUtils.checkFinite(123.45);
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.POSITIVE_INFINITY));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NaN));
    }

    @Test
    public void testCheckFiniteArray() {
        MathUtils.checkFinite(new double[]{1.5, -2.0, 3.7});
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(new double[]{Double.POSITIVE_INFINITY, 2.0, 3.7}));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(new double[]{1.5, Double.NaN, 3.7}));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(new double[]{1.5, -2.0, Double.NaN}));
    }

    @Test
    public void testCheckNotNull() {
        MathUtils.checkNotNull(new Object());
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null));
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null, LocalizedFormats.SOME_MESSAGE));
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null, LocalizedFormats.SOME_MESSAGE, "arg1", "arg2"));
    }

}