import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer1 = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        assertNotNull(result1);
        
        // Test case 2
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.01);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        assertNotNull(result2);
        
        // Test case 3
        BrentOptimizer optimizer3 = new BrentOptimizer(0.1, 0.1);
        UnivariatePointValuePair result3 = optimizer3.doOptimize();
        assertNotNull(result3);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(2.0, 3.0);
        UnivariatePointValuePair bestPoint = optimizer.best(point1, point2, true);
        assertEquals(point1, bestPoint);
    }
}