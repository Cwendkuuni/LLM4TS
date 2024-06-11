package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

    /**
     * Test function for Brent optimization algorithm.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * (Math.cos(x) * 2 * Math.sin(x) - Math.sin(x) * 2 * Math.cos(x));
        }
    }

    /**
     * Test for Brent Optimizer within acceptable tolerance.
     */
    @Test
    public void testBrentOptimizer() {
        TestFunction testFunction = new TestFunction();
        double initialGuess = 1.0;
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        PointValuePair result = optimizer.optimize(testFunction, initialGuess, null, null);

        assertNotNull(result);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, testFunction.value(result.getPoint()), 1e-6);
    }

    /**
     * Test for Brent Optimizer with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidInput() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = 0.0;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
    }

    /**
     * Test for Brent Optimizer with positive absolute threshold.
     */
    @Test
    public void testBrentOptimizerPositiveAbsoluteThreshold() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-5;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        PointValuePair result = optimizer.optimize(testFunction, 1.0, null, null);

        assertNotNull(result);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, testFunction.value(result.getPoint()), 1e-6);
    }

    /**
     * Test for Brent Optimizer with negative relative threshold.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = -1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
    }

    /**
     * Test for Brent Optimizer with zero relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerZeroRelativeThreshold() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = 0.0;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
    }

    /**
     * Test for Brent Optimizer with invalid initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidInitialGuess() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.optimize(testFunction, Double.NEGATIVE_INFINITY, null, null);
    }

    /**
     * Test for Brent Optimizer with custom convergence checker.
     */
    @Test
    public void testBrentOptimizerCustomConvergenceChecker() {
        TestFunction testFunction = new TestFunction();
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = (iter, oldPoint, newPoint) -> newPoint.getValue() < oldPoint.getValue();

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        PointValuePair result = optimizer.optimize(testFunction, 1.0, null, null);

        assertNotNull(result);
        assertEquals(-1.0, result.getPoint(), 1e-6);
        assertEquals(0.0, testFunction.value(result.getPoint()), 1e-6);
    }

    /**
     * Test for Brent Optimizer with function that has no solution.
     */
    @Test
    public void testBrentOptimizerNoSolution() {
        ObjectiveFunction testFunction = x -> Math.sin(x) / x;
        double initialGuess = 1.0;
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        PointValuePair result = optimizer.optimize(testFunction, initialGuess, null, null);

        assertNotNull(result);
        assertEquals(Double.NaN, result.getPoint(), 1e-6);
        assertEquals(Double.NaN, testFunction.value(result.getPoint()), 1e-6);
    }

    /**
     * Test for Brent Optimizer with a goal type other than minimization.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWrongGoalType() {
        TestFunction testFunction = x -> Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        double initialGuess = 1.0;
        double relativeThreshold = 1e-6;
        double absoluteThreshold = 1e-6;
        ConvergenceChecker<PointValuePair> convergenceChecker = null;

        UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(testFunction, initialGuess, null, null);
    }
}