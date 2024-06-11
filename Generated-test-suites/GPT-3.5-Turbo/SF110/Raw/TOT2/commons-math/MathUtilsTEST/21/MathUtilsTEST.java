import static org.junit.Assert.*;
import org.junit.Test;

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
        double[] values = {1.2, 3.4, 5.6};
        int expected = Arrays.hashCode(values);
        
        int actual = MathUtils.hash(values);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 7.5;
        double center = 3.0;
        double expected = 4.5;
        
        double actual = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 5.0;
        double offset = 2.0;
        double expected = 3.5;
        
        double actual = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, actual, 0.001);
    }
    
    @Test
    public void testCopySign_byte() {
        byte magnitude = 10;
        byte sign = -5;
        byte expected = 10;
        
        byte actual = MathUtils.copySign(magnitude, sign);
        
        assertEquals(expected, actual);
    }
    
    // Additional test cases for edge cases and exceptions can be added here

}