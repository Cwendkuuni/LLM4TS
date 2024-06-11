import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        // Test the optimize method with typical values
        // Set up the test case
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BaseUnivariateRealOptimizer(), 5, new RandomGenerator());
        // Define the objective function and goal type
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4; // Equation: x^2 - 4x + 4
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        
        // Call the optimize method
        UnivariateRealPointValuePair result = optimizer.optimize(function, goalType, min, max);

        // Assert the result
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(1.0, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeNoConvergence() {
        // Test the optimize method when no convergence is achieved
        // Set up the test case
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BaseUnivariateRealOptimizer(), 1, new RandomGenerator());
        // Define the objective function and goal type
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = Math.PI;
        
        // Call the optimize method and expect a ConvergenceException
        assertThrows(ConvergenceException.class, () -> {
            optimizer.optimize(function, goalType, min, max);
        });
    }

    @Test
    public void testGetOptima() {
        // Test the getOptima method
        // Set up the test case
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BaseUnivariateRealOptimizer(), 3, new RandomGenerator());
        // Call the optimize method
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        optimizer.optimize(function, goalType, min, max);
        
        // Get the optima
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        
        // Assert the length of the optima array
        assertEquals(3, optima.length);
    }
}