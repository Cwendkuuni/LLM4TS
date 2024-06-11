import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.optimizers.SimplexOptimizer;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    @Test
    public void testOptimize() {
        double[] startPoint = {-1.0, -1.0};
        double[] expectedOptimum = {1.0, 1.0};
        
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        PointValuePair optimum = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        
        assertArrayEquals(expectedOptimum, optimum.getPoint(), 1e-5);
        assertEquals(0.0, optimum.getValue(), 1e-5);
    }
}