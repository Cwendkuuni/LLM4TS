import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 10.5;
        int expected = Double.valueOf(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.5, 3.7};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 3.0;
        double expected = a - 6.283185307179586 * FastMath.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 2.0;
        double offset = 1.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.0001);
    }

    // More test cases can be added for copySign, checkFinite, and checkNotNull methods

}