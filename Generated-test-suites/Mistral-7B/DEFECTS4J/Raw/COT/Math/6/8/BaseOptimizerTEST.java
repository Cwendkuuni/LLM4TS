package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optim.PointValuePair;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expectedMaxEvaluations = 10;
        assertEquals(expectedMaxEvaluations, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        optimizer.optimize(new MaxEval(10, null));
        int evaluations = optimizer.getEvaluations();
        int expectedEvaluations = 10;
        assertEquals(expectedEvaluations, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with negative value
     */
    @Test(expected = IllegalStateException.class)
    public void testNegativeMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with negative value
     */
    @Test(expected = IllegalStateException.class)
    public void testNegativeEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        optimizer.setEvaluations(-1);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with null value
     */
    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new BaseMockOptimizer();
        optimizer.setConvergenceChecker(null);
    }
}

class BaseMockOptimizer implements BaseOptimizer<PointValuePair> {
    private int maxEvaluations = 10;
    private int evaluations = 0;
    private ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
        @Override
        public boolean isConverged(PointValuePair pointValuePair) {
            return false;
        }
    };

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return convergenceChecker;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public PointValuePair optimize(MaxEval maxEval) throws OptimizationException {
        return new PointValuePair(new double[0], Double.POSITIVE_INFINITY);
    }
}