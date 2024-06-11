import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    // Test case for optimize method with typical use case
    @Test
    public void testOptimizeTypical() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 5, new RandomGeneratorImpl());
        UnivariateRealFunction f = new TestFunction();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
        assertEquals(0.0, result, 0.01);
    }

    // Test case for optimize method with edge case of empty function
    @Test
    public void testOptimizeEmptyFunction() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 5, new RandomGeneratorImpl());
        UnivariateRealFunction f = new EmptyFunction();
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        }
    }

    // Test case for optimize method with edge case of unbounded range
    @Test
    public void testOptimizeUnboundedRange() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 5, new RandomGeneratorImpl());
        UnivariateRealFunction f = new TestFunction();
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(10.0, result, 0.01);
    }

    // Test case for optimize method with error scenario of non-convergence
    @Test
    public void testOptimizeNonConvergence() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new UnivariateRealOptimizerImpl(), 5, new RandomGeneratorImpl());
        UnivariateRealFunction f = new NonConvergingFunction();
        try {
            optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException");
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        }
    }
}