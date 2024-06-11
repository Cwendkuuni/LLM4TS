import org.junit.*;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHash_Double() {
        double value = 10.5;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 4.2;
        double center = 2.0;
        double expected = 2.2;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, result, 0.0001);
    }
    
    @Test
    public void testReduce() {
        double a = 15;
        double period = 3;
        double offset = 2;
        double expected = 1;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 0.0001);
    }
    
    // Additional test cases for other methods can be added below
}