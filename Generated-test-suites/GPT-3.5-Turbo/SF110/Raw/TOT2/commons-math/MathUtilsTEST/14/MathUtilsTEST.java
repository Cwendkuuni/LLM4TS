import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MathUtilsTest {

    @Test
    public void testHash_double() {
        double value = 5.0;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHash_doubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int actual = MathUtils.hash(value);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 2.0;
        double expected = a - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        double actual = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, actual, 0.0001);
    }
    
    // Additional test cases for other methods can be added here
    
}