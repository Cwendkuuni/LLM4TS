import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private final double EPSILON = 1e-6;

    @Test
    public void testOptimize() {
        // Create an instance of the optimizer
        BaseUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizer(), 5, new RandomGenerator());

        // Define the objective function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        // Set the goal type to maximize
        GoalType goal = GoalType.MAXIMIZE;

        // Define the bounds for optimization
        double min = -10;
        double max = 10;

        // Optimize the function
        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);

        // Check the result
        assertEquals(100.0, result.getValue(), EPSILON);
        assertEquals(10.0, result.getPoint(), EPSILON);
    }

    @Test
    public void testOptimizeWithException() {
        // Create an instance of the optimizer
        BaseUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizer(), 5, new RandomGenerator());

        // Define the objective function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation exception");
            }
        };

        // Set the goal type to maximize
        GoalType goal = GoalType.MAXIMIZE;

        // Define the bounds for optimization
        double min = -10;
        double max = 10;

        // Optimize the function
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
            fail("FunctionEvaluationException should have been thrown");
        } catch (FunctionEvaluationException e) {
            // Exception properly thrown
        }
    }

    @Test
    public void testOptimizeWithConvergenceException() {
        // Create an instance of the optimizer
        BaseUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizer(), 5, new RandomGenerator());

        // Define the objective function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        // Set the goal type to maximize
        GoalType goal = GoalType.MAXIMIZE;

        // Define the bounds for optimization
        double min = -10;
        double max = 10;

        // Set the maximum number of evaluations to 1
        optimizer.setMaxEvaluations(1);

        // Optimize the function
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);
            fail("ConvergenceException should have been thrown");
        } catch (ConvergenceException e) {
            // Exception properly thrown
        }
    }
}