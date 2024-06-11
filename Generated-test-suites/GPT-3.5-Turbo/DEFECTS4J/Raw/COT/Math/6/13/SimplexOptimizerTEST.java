
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Initialize SimplexOptimizer with a convergence checker
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Assert that the optimizer is not null
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithThresholds() {
        // Initialize SimplexOptimizer with relative and absolute thresholds
        SimplexOptimizer optimizer = new SimplexOptimizer(0.001, 0.01);

        // Assert that the optimizer is not null
        assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        // Initialize SimplexOptimizer
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Create a Simplex object
        Simplex simplex = new Simplex();

        // Set the simplex on the optimizer
        optimizer.setSimplex(simplex);

        // Assert that the simplex on the optimizer is not null
        assertNotNull(optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullArgument() {
        // Initialize SimplexOptimizer
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Set null simplex on the optimizer
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimize() {
        // Initialize SimplexOptimizer
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());

        // Create a MultivariateFunction
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                // Define the objective function
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        // Set the start point for optimization
        double[] startPoint = {1.0, 2.0};

        // Set the goal type for optimization
        GoalType goalType = GoalType.MINIMIZE;

        // Optimize the function
        PointValuePair result = optimizer.optimize(100, function, goalType, startPoint);

        // Check if the optimization result is within the specified thresholds
        assertTrue(optimizer.getConvergenceChecker().converged(optimizer.getEvaluations(), result, optimizer.getStartPoint()));

        // Assert that the result is not null
        assertNotNull(result);
    }

}
