import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MathUtilsTest {
    
    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = new Double(value).hashCode();
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashDoubleArray() {
        double[] value = {1.0, 2.0, 3.0};
        int expected = Arrays.hashCode(value);
        int result = MathUtils.hash(value);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNormalizeAngle() {
        double a = 10.0;
        double center = 5.0;
        double expected = 10.0 - 6.283185307179586 * FastMath.floor((10.0 + 3.141592653589793 - 5.0) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(a, center);
        
        assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testReduce() {
        double a = 15.0;
        double period = 10.0;
        double offset = 5.0;
        double expected = 0.0;
        double result = MathUtils.reduce(a, period, offset);
        
        assertEquals(expected, result, 0.0);
    }
    
    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -3;
        byte expected = -5;
        
        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, result);
        } catch (MathArithmeticException e) {
            fail("An exception should not occur in this scenario");
        }
    }
    
    @Test
    public void testCheckFinite() {
        double x = Double.POSITIVE_INFINITY;
        
        try {
            MathUtils.checkFinite(x);
            fail("An exception should be thrown for infinite number");
        } catch (NotFiniteNumberException e) {
            assertThat(e.getMessage(), containsString("Not a finite number"));
        }
    }
}