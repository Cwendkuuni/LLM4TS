package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleConstraints;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.univariate.convergence.RhoSigmaChecker;
import org.apache.commons.math3.optim.univariate.nonlinear.GradientDescent;
import org.apache.commons.math3.optim.univariate.nonlinear.LevenbergMarquardt;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BaseOptimizer}.
 *
 * @since 3.1
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new GradientDescent();
        int maxEvaluations = optimizer.getMaxEvaluations();
        int expected = MaxEval.MAX_EVAL;
        assertEquals("Incorrect maximal number of function evaluations.", expected, maxEvaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = new GradientDescent();
        int evaluations = optimizer.getEvaluations();
        int expected = 0;
        assertEquals("Incorrect number of function evaluations.", expected, evaluations);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.abs(x)) - Math.log(Math.abs(x) + 1);
            }

            @Override
            public double derivative(double x) {
                return (Math.E * Math.pow(Math.E, Math.abs(x))) / (Math.abs(x) + 1) - 1 / (Math.abs(x) + 1);
            }
        };

        optimizer.optimize(function, 0.0, 1.0, new SimpleConstraints());

        evaluations = optimizer.getEvaluations();
        expected = MaxEval.MAX_EVAL;
        assertEquals("Incorrect number of function evaluations.", expected, evaluations);
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = new GradientDescent();
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();

        assertFalse("Convergence checker should not be initialized.", convergenceChecker instanceof RhoSigmaChecker);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize(UnivariateFunction, double, double, SimpleConstraints)} with Levenberg-Marquardt optimizer.
     */
    @Test
    public void testOptimizeWithLevenbergMarquardt() {
        BaseOptimizer<PointValuePair> optimizer = new LevenbergMarquardt();
        ConvergenceChecker<PointValuePair> convergenceChecker = new AbstractConvergenceChecker<PointValuePair>() {
            @Override
            public boolean check(PointValuePair currentBest, PointValuePair currentPoint) {
                double currentBestValue = currentBest.getValue();
                double currentValue = currentPoint.getValue();
                return (Math.abs(currentBestValue - currentValue) < 1e-6);
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return 2 * x + Math.cos(Math.PI * x);
            }
        };

        PointValuePair result = optimizer.optimize(function, 0.0, 10.0, new SimpleConstraints());
        double expectedX = 1.1213203435588327;
        double expectedValue = 0.0;
        assertEquals("Incorrect optimization result.", expectedX, result.getPoint(), 1e-6);
        assertEquals("Incorrect optimization result.", expectedValue, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize(UnivariateFunction, double, double, SimpleConstraints)} with GradientDescent optimizer.
     */
    @Test
    public void testOptimizeWithGradientDescent() {
        BaseOptimizer<PointValuePair> optimizer = new GradientDescent();
        ConvergenceChecker<PointValuePair> convergenceChecker = new AbstractConvergenceChecker<PointValuePair>() {
            @Override
            public boolean check(PointValuePair currentBest, PointValuePair currentPoint) {
                double currentBestValue = currentBest.getValue();
                double currentValue = currentPoint.getValue();
                return (Math.abs(currentBestValue - currentValue) < 1e-6);
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x);
            }

            @Override
            public double derivative(double x) {
                return 2 * x + Math.cos(Math.PI * x);
            }
        };

        PointValuePair result = optimizer.optimize(function, 0.0, 10.0, new SimpleConstraints());
        double expectedX = 1.1213203435588327;
        double expectedValue = 0.0;
        assertEquals("Incorrect optimization result.", expectedX, result.getPoint(), 1e-6);
        assertEquals("Incorrect optimization result.", expectedValue, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link BaseOptimizer#optimize(UnivariateFunction, double, double, SimpleConstraints)} with unconstrained function.
     */
    @Test
    public void testOptimizeWithUnconstrainedFunction() {
        BaseOptimizer<PointValuePair> optimizer = new GradientDescent();
        ConvergenceChecker<PointValuePair> convergenceChecker = new AbstractConvergenceChecker<PointValuePair>() {
            @Override
            public boolean check(PointValuePair currentBest, PointValuePair currentPoint) {
                double currentBestValue = currentBest.getValue();
                double currentValue = currentPoint.getValue();
                return (Math.abs(currentBestValue - currentValue) < 1e-6);
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        PointValuePair result = optimizer.optimize(function, 0.0, 1.0, null);
        double expectedX = 0.5;
        double expectedValue = 0.25;
        assertEquals("Incorrect optimization result.", expectedX, result.getPoint(), 1e-6);
        assertEquals("Incorrect optimization result.", expectedValue, result.getValue(), 1e-6);
    }
}