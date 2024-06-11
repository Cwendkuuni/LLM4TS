import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHash_doubleArray() {
        double[] value = {1.5, 2.5, 3.5};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 5;
        double expected = -5.3;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2.5;
        double offset = 1.5;
        double expected = 0.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySign_byte() {
        byte magnitude = 100;
        byte sign = -50;
        MathUtils.copySign(magnitude, sign);
    }

    // Additional test cases for other copySign methods and exception handling can be added here

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    // Additional test cases for checkFinite and checkNotNull methods can be added here

    @Test
    public void testCheckNotNull() {
        Object o = new Object();
        try {
            MathUtils.checkNotNull(o);
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException");
        }
    }

}