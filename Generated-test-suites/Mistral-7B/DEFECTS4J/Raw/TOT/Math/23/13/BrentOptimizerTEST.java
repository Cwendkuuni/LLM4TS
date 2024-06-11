package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * A simple test function.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 4 * x + 4;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Test optimization of a function with a known minimum.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        double relTol = 1e-6;
        double absTol = 1e-6;
        ConvergenceChecker<PointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        TestFunction function = new TestFunction();

        PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(-2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(4.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a non-strictly positive absolute tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeWithNonPositiveAbsoluteTolerance() {
        double relTol = 1e-6;
        double absTol = -1e-6;
        ConvergenceChecker<PointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        TestFunction function = new TestFunction();

        optimizer.optimize(function, -5.0, 5.0);
    }

    /**
     * Test optimization with a non-strictly positive relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeWithNonPositiveRelativeTolerance() {
        double relTol = -1e-6;
        double absTol = 1e-6;
        ConvergenceChecker<PointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        TestFunction function = new TestFunction();

        optimizer.optimize(function, -5.0, 5.0);
    }

    /**
     * Test optimization of a function with a known maximum.
     */
    @Test
    public void testOptimizeMaxFunction() {
        double relTol = 1e-6;
        double absTol = 1e-6;
        ConvergenceChecker<PointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        TestFunction function = new TestFunctionMax();

        PointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(4.0, result.getValue(), 1e-6);
    }

    /**
     * A simple test function with a known maximum.
     */
    private static class TestFunctionMax implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) + 4 * x + 4;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MAXIMIZE;
        }
    }

    /**
     * Test optimization with a non-converging function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNonConvergingFunction() {
        double relTol = 1e-6;
        double absTol = 1e-6;
        ConvergenceChecker<PointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, checker);
        TestFunctionNonConverging function = new TestFunctionNonConverging();

        optimizer.optimize(function, -5.0, 5.0);
    }

    /**
     * A non-converging test function.
     */
    private static class TestFunctionNonConverging implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.sin(Math.PI * x) / (Math.PI * x);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }
}