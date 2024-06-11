import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);
        
        // Test case 1
        double[] startPoint = {1, 1};
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        PointValuePair expected = new PointValuePair(new double[]{0, 0}, 0);
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(expected, result);
        
        // Test case 2
        startPoint = new double[]{10, 5};
        function = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + 2 * point[1] * point[1];
            }
        };
        expected = new PointValuePair(new double[]{0, 0}, 0);
        result = optimizer.optimize(function, startPoint);
        assertEquals(expected, result);
    }
}