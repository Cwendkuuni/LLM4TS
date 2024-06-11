package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optim.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for {@link MultiStartUnivariateRealOptimizer}.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) {
            return -2 * Math.sin(x) * Math.cos(x);
        }
    }

    /**
     * Test optimization with multiple starts.
     *
     * @throws MaxEvalException        if maximum number of evaluations is reached.
     * @throws ConvergenceException    if convergence criterion is not met.
     */
    @Test
    public void testMultiStartOptimization() throws MaxEvalException, ConvergenceException {
        // Create a test function.
        UnivariateRealFunction f = new TestFunction();

        // Set up a random number generator.
        RandomGenerator rng = new org.apache.commons.math3.random.MersenneTwisterFastRandom();

        // Set up a multi-start optimizer.
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new FibonacciSearchUnivariateRealOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, rng);

        // Optimize the function with multiple starts.
        GoalType goal = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        UnivariateRealPointValuePair bestPoint = multiStartOptimizer.optimize(f, goal, min, max);

        // Check the result.
        assertNotNull(bestPoint);
        double expectedMinimum = -3.141592653589793;
        assertEquals(expectedMinimum, bestPoint.getPoint(), 1e-6);
        assertEquals(expectedMinimum, bestPoint.getValue(), 1e-6);

        // Check all found optima.
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(10, optima.length);
        for (UnivariateRealPointValuePair optimum : optima) {
            assertNotNull(optimum);
            if (optimum != bestPoint) {
                assertNull(optimum.getPoint());
            }
        }
    }
}