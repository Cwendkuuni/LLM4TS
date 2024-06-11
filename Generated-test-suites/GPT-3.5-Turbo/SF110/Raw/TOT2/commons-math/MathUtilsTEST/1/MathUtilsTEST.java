import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashValue() {
        double value = 5.0;
        assertEquals(73056572, MathUtils.hash(value));
    }
    
    @Test
    public void testHashArray() {
        double[] value = {5.0, 10.0};
        assertEquals(73171463, MathUtils.hash(value));
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 0.5;
        double center = 0;
        assertEquals(0.5, MathUtils.normalizeAngle(a, center), 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.0;
        double period = 3.0;
        double offset = 2.0;
        assertEquals(1, MathUtils.reduce(a, period, offset), 0.0001);
    }
    
    @Test(expected = MathArithmeticException.class)
    public void testCopySignException() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullException() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }
}