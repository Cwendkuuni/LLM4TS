import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        // Test for basic functionality of optimize method
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BrentOptimizer(), 5, new Well19937c(100));
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goal = GoalType.MINIMIZE;
        
        try {
            UnivariateRealPointValuePair result = optimizer.optimize(function, goal, 0, 5);
            assertNotNull(result);
            assertEquals(0.0, result.getPoint(), 1e-6);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception occurred");
        }
    }

    @Test
    public void testGetOptima() {
        // Test for getting optima after optimization
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new BrentOptimizer(), 5, new Well19937c(100));
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goal = GoalType.MINIMIZE;
        
        try {
            optimizer.optimize(function, goal, 0, 5);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();
            assertNotNull(optima);
            assertEquals(5, optima.length);
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation exception occurred");
        }
    }

    // Add more tests for other methods and edge cases

}