package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractTests;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Brent minimizer class.
 */
public class BrentOptimizerTest extends AbstractTests {

    /**
     * Test the optimizer with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(f, 2.0);
        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(-3.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test the optimizer with a non-strictly positive threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonStrictlyPositiveThreshold() {
        new BrentOptimizer(-1.0, 1.0e-6, null);
    }

    /**
     * Test the optimizer with a threshold smaller than machine precision.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testThresholdSmallerThanMachinePrecision() {
        new BrentOptimizer(1.0e-10, 1.0e-6, null);
    }

    /**
     * Test the optimizer with a goal other than minimization.
     */
    @Test
    public void testMaximization() {
        UnivariateFunction f = new MyFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(f, 2.0);
        assertEquals(3.0, result.getPoint(), 1.0e-6);
        assertEquals(3.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test the optimizer with a user-defined convergence checker.
     */
    @Test
    public void testConvergenceChecker() {
        UnivariateFunction f = new MyFunction();
        MyConvergenceChecker checker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(f, 2.0);
        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(-3.0, result.getValue(), 1.0e-6);
        // Check that convergence was detected.
        assertTrue(checker.wasConverged);
    }

    /**
     * A simple test function for the optimizer.
     */
    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 2.0 * x;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    /**
     * A convergence checker for the optimizer tests.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        boolean wasConverged = false;

        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            wasConverged = true;
            return iter > 10;
        }
    }
}