package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnconvergedException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergenceException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    private BaseOptimizer<PointValuePair> optimizer;

    @Before
    public void setUp() {
        optimizer = (BaseOptimizer<PointValuePair>) new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(final double[] x, final InitialGuess initialGuess, final MaxEval maxEval, final ConvergenceChecker<PointValuePair> convergenceChecker) {
                // This is just a dummy implementation for test purposes.
                return new PointValuePair(0.0, 0.0);
            }
        };
        optimizer.setMaxEvaluations(10);
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals("Incorrect maximum number of evaluations.", 10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() throws UnconvergedException, NonConvergenceException {
        int initialEvaluations = optimizer.getEvaluations();
        Assert.assertEquals("Initial number of evaluations should be zero.", 0, initialEvaluations);

        optimizer.optimize(new double[]{0.0}, new InitialGuess(0.0), new MaxEval(5), getConvergenceChecker());
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Number of evaluations after optimization is incorrect.", 5, evaluations);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerUnsupported() {
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull("Convergence checker should not be null.", convergenceChecker);
    }

    @Test(expected = IllegalStateException.class)
    public void testNegativeMaxEvaluations() {
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testMaxEvaluationsGreaterThanExpected() {
        optimizer.setMaxEvaluations(15);
        optimizer.optimize(new double[]{0.0}, new InitialGuess(0.0), new MaxEval(10), getConvergenceChecker());
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Number of evaluations after optimization is less than expected.", 10, evaluations);
    }

    @Test
    public void testZeroMaxEvaluations() {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(new double[]{0.0}, new InitialGuess(0.0), getMaxEval(0), getConvergenceChecker());
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals("Number of evaluations after optimization is greater than expected.", 0, evaluations);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(final PointValuePair currentValue, final PointValuePair previousValue) {
                // custom implementation for test purpose
                return true;
            }
        };
        optimizer.setConvergenceChecker(customConvergenceChecker);
        Assert.assertSame("Set convergence checker failed.", customConvergenceChecker, optimizer.getConvergenceChecker());
    }

    private MaxEval getMaxEval(final int maxEvaluations) {
        return new MaxEval(maxEvaluations);
    }

    private ConvergenceChecker<PointValuePair> getConvergenceChecker() {
        return new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(final PointValuePair currentValue, final PointValuePair previousValue) {
                return currentValue.getValue() >= 0.0 && Math.abs(currentValue.getValue() - previousValue.getValue()) <= 0.001;
            }
        };
    }
}