package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.noderoutines.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BaseOptimizer}.
 *
 * @since 3.0
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BrentOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Maximum number of evaluations should be set to a default value", MaxEval.DEFAULT.getValue(), maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BrentOptimizer();
        UnivariateOptimizer<Double> optimized = (UnivariateOptimizer<Double>) optimizer;
        optimized.optimize(x -> Math.pow(x, 2), new SimpleBounds(0.0, 1.0), null);
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be greater than zero after optimization", 1, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BrentOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull("Convergence checker should be defined", convergenceChecker);

        // Test default ConvergenceChecker implementation
        PointValuePair result = optimizer.optimize(f -> Math.exp(-f.getPoint()) - E.E, new SimpleBounds(0.0, 5.0), null);
        boolean isConverged = convergenceChecker.isConverged(result, 1.0E-10);
        assertTrue("Function with large convergence criterion should be considered converged", isConverged);

        // Test custom ConvergenceChecker implementation
        optimizer = new BrentOptimizer() {
            @SuppressWarnings("unchecked")
            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return new ConvergenceChecker<PAIR>() {
                    @Override
                    public boolean isConverged(PAIR solution, double tolerance) {
                        return solution.getValue() <= 1.0E-15;
                    }
                };
            }
        };
        result = optimizer.optimize(x -> Math.pow(x, 2), new SimpleBounds(-1.0, 1.0), null);
        isConverged = convergenceChecker.isConverged(result, 1.0E-20);
        assertFalse("Function with small convergence criterion should NOT be considered converged", isConverged);
    }
}