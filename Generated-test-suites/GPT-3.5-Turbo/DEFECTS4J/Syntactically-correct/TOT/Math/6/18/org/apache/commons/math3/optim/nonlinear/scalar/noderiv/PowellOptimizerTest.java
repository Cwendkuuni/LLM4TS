import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize_minimumValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        double[] startPoint = {0, 0};
        double[] expectedPoint = {0, 0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0);
        PointValuePair result = optimizer.doOptimize(startPoint);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoOptimize_maximumValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        double[] startPoint = {Double.MAX_VALUE, Double.MAX_VALUE};
        double[] expectedPoint = {0, 0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0);
        PointValuePair result = optimizer.doOptimize(startPoint);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoOptimize_largeValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        double[] startPoint = {1e10, 1e10};
        double[] expectedPoint = {0, 0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0);
        PointValuePair result = optimizer.doOptimize(startPoint);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoOptimize_smallValue() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        double[] startPoint = {1e-10, 1e-10};
        double[] expectedPoint = {0, 0};
        PointValuePair expected = new PointValuePair(expectedPoint, 0);
        PointValuePair result = optimizer.doOptimize(startPoint);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoOptimize_errorScenario() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        double[] startPoint = null;
        PointValuePair expected = null;
        PointValuePair result = optimizer.doOptimize(startPoint);
        assertEquals(expected, result);
    }
}