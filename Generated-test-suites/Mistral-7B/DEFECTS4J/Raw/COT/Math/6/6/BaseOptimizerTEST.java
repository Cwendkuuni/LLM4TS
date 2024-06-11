package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

/**
 * Test class for {@link BaseOptimizer} interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals("Max evaluations should be defined", 10, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        optimizer.optimize(new PointValuePair(0.0, 1.0));
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be 1", 1, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull("Convergence checker should be null", convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} before optimization.
     */
    @Test
    public void testGetEvaluationsBeforeOptimization() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        int evaluations = optimizer.getEvaluations();
        assertEquals("Number of evaluations should be 0 before optimization", 0, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with custom implementation.
     */
    @Test
    public void testSetCustomConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = getDummyBaseOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(PointValuePair currentValue, PointValuePair previousValue, int iteration) {
                return false;
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
        ConvergenceChecker<PointValuePair> testChecker = optimizer.getConvergenceChecker();
        assertNotNull("Custom convergence checker should not be null", testChecker);
    }

    /**
     * Helper method to create a dummy {@link BaseOptimizer}.
     *
     * @return a dummy {@link BaseOptimizer} instance.
     */
    private BaseOptimizer<PointValuePair> getDummyBaseOptimizer() {
        return new BaseOptimizer<PointValuePair>() {
            private int evaluations = 0;

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public int getEvaluations() {
                return evaluations;
            }

            @Override
            public int getMaxEvaluations() {
                return 10;
            }

            @Override
            public void optimize(PointValuePair initialGuess) {
                evaluations++;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<PointValuePair> newChecker) {
                // empty implementation
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                // empty implementation
            }
        };
    }
}