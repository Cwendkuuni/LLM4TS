import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSetSimplex() {
        // Create SimplexOptimizer object
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create AbstractSimplex object
        AbstractSimplex simplex = new AbstractSimplex();

        // Set simplex in optimizer
        optimizer.setSimplex(simplex);

        // Get simplex from optimizer
        AbstractSimplex actualSimplex = optimizer.getSimplex();

        // Assert that the simplex is set correctly
        assertEquals(simplex, actualSimplex);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        // Create SimplexOptimizer object
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Call doOptimize method without setting simplex
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        // Create SimplexOptimizer object
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Create AbstractSimplex object
        AbstractSimplex simplex = new AbstractSimplex();

        // Set simplex in optimizer
        optimizer.setSimplex(simplex);

        // Create test data
        double[] startPoint = { 1.0, 3.0, 5.0 };
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Optimize the function
        PointValuePair result = optimizer.optimize(100, f, goalType, startPoint);

        // Check the result
        double[] expectedPoint = { 0.0, 0.0, 0.0 };
        PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
        assertEquals(expected, result);
    }
}