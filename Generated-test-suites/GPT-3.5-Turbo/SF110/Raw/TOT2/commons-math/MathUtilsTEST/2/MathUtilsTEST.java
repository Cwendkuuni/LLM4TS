import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 10.0;
        int expected = new Double(10.0).hashCode();
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        assertEquals(expected, MathUtils.hash(value));
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 5.0;
        double expected = 4.7107963267948966;
        assertEquals(expected, MathUtils.normalizeAngle(a, center), 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        double expected = 1.0;
        assertEquals(expected, MathUtils.reduce(a, period, offset), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10; 
        byte sign = -5;
        byte expected = -10;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 100; 
        short sign = -50;
        short expected = -100;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000; 
        int sign = -500;
        int expected = -1000;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 10000L;
        long sign = -5000L;
        long expected = -10000L;
        assertEquals(expected, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFinite() {
        double x = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(x));
    }

    @Test
    public void testCheckFiniteArray() {
        double[] val = {1.0, Double.NaN, 3.0};
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(val));
    }

    @Test
    public void testCheckNotNull() {
        Object o = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o));
    }

    @Test
    public void testCheckNotNullWithPattern() {
        Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(o, pattern, args));
    }
}