import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);

        // Test the doOptimize method
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Verify the result
        assertEquals(0.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBest() {
        // Create some test points
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(2.0, 3.0);
        UnivariatePointValuePair point3 = new UnivariatePointValuePair(3.0, 1.0);

        // Test the best method
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        UnivariatePointValuePair bestPoint = optimizer.best(point1, point2, true);

        // Verify the result
        assertEquals(point1.getPoint(), bestPoint.getPoint(), 1e-6);
    }
}