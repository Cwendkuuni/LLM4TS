import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        
        // Test case 1
        double expected1 = 0.0;
        double result1 = optimizer.doOptimize().getPoint();
        assertEquals(expected1, result1, 0.001);
        
        // Test case 2
        double expected2 = 1.0;
        double result2 = optimizer.doOptimize().getPoint();
        assertEquals(expected2, result2, 0.001);
        
        // Test case 3
        double expected3 = -1.0;
        double result3 = optimizer.doOptimize().getPoint();
        assertEquals(expected3, result3, 0.001);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        
        // Test case 1
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair result1 = optimizer.best(point1, point2, true);
        assertEquals(point1, result1);
        
        // Test case 2
        UnivariatePointValuePair point3 = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair point4 = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair result2 = optimizer.best(point3, point4, false);
        assertEquals(point3, result2);
    }
}