import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizer() {
        // Create a SimplexOptimizer instance
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Define a test MultivariateFunction
        MultivariateFunction testFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                // Define a simple test function, f(x) = x^2
                return Math.pow(point[0], 2);
            }
        };

        // Define the start point for optimization
        double[] startPoint = {5.0}; // Starting at x=5

        // Optimize the test function using the SimplexOptimizer
        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, startPoint);

        // Ensure that the optimal value is close to 0 (minimum of x^2)
        assertEquals(0.0, result.getValue(), 0.001);
        // Ensure that the optimal point is close to 0
        assertEquals(0.0, result.getPoint()[0], 0.001);
    }

    @Test
    public void testSimplexOptimizerWithNullSimplex() {
        // Create a SimplexOptimizer instance with no simplex
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        // Define a test MultivariateFunction
        MultivariateFunction testFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        };

        // Define the start point for optimization
        double[] startPoint = {0.0};

        // Try to optimize without setting a simplex (should throw NullArgumentException)
        try {
            PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, startPoint);
            fail("Expected NullArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected Exception
        }
    }
}