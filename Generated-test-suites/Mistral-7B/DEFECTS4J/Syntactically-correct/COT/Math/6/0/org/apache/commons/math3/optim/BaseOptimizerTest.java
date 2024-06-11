package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValues;
import org.apache.commons.math3.optim.UnivariateConverter;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals("Incorrect maximum number of function evaluations.", 100, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        optimizer.optimize(new TestFunction(), new SimpleBounds(0.0, 1.0), new InitialGuess(0.5), new MaxEval(10));
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Incorrect number of function evaluations.", 11, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull("Convergence checker should not be null.", convergenceChecker);
    }

    /**
     * Test case for {@link BaseOptimizer#getMaxEvaluations()} with minimum value.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetMaxEvaluationsMinimum() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        optimizer.setMaxEvaluations(-1);
        int maxEvaluations = optimizer.getMaxEvaluations();
    }

    /**
     * Test case for {@link BaseOptimizer#getEvaluations()} with negative value.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetEvaluationsNegative() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        optimizer.setEvaluations(-1);
        int evaluations = optimizer.getEvaluations();
    }

    /**
     * Test case for {@link BaseOptimizer#getConvergenceChecker()} with null value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PointValuePair> optimizer = new DummyOptimizer();
        optimizer.setConvergenceChecker(null);
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
    }

    /**
     * Dummy optimizer for testing purposes.
     */
    public static class DummyOptimizer implements BaseOptimizer<PointValuePair> {
        private int maxEvaluations = 100;

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        private int evaluations = 0;

        @Override
        public int getEvaluations() {
            return evaluations;
        }

        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }

        private ConvergenceChecker<PointValuePair> convergenceChecker;

        @Override
        public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
            return convergenceChecker;
        }

        public void setConvergenceChecker(ConvergenceChecker<PointValuePair> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public PointValuePair optimize(UnivariateFunction f, SimpleBounds bounds, InitialGuess initialGuess, MaxEval maxEvals) {
            PointValuePair result = new PointValuePair(0.5, 0.0);
            evaluations += maxEvals.getMax();
            return result;
        }
    }

    /**
     * Test function for testing purposes.
     */
    public static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }
}