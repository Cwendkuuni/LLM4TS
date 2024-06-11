package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Unit tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {
    /**
     * Test constructor with relative and absolute tolerance and null
     * convergence checker.
     */
    @Test
    public void testConstructorWithTolerances() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with relative and absolute tolerance and custom
     * convergence checker.
     */
    @Test
    public void testConstructorWithTolerancesAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with only relative tolerance and null convergence
     * checker.
     */
    @Test
    public void testConstructorWithRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(0d, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with only absolute tolerance and null convergence
     * checker.
     */
    @Test
    public void testConstructorWithAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertEquals(0d, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test constructor with relative and absolute tolerance and custom
     * convergence checker that always returns "converged".
     */
    @Test
    public void testConstructorWithTolerancesAndAlwaysConvergedChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6,
                new MyAlwaysConvergedChecker());
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testSimpleFunctionOptimization() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(-1.5, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a simple function and custom convergence
     * checker that always returns "converged".
     */
    @Test
    public void testSimpleFunctionOptimizationWithAlwaysConvergedChecker() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6,
                new MyAlwaysConvergedChecker());
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(-1.5, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a simple function and custom convergence
     * checker that returns "converged" only after 10 iterations.
     */
    @Test
    public void testSimpleFunctionOptimizationWithIterationLimitChecker() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6,
                new MyIterationLimitChecker(10));
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(-1.5, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a function that has a non-strictly positive
     * absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonStrictlyPositiveAbsoluteThreshold() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6);
        optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
    }

    /**
     * Test optimization with a function that has a non-positive relative
     * threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNonPositiveRelativeThreshold() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0d, 1e-6);
        optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
    }

    /**
     * Test optimization with a function that has a negative goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeGoalType() {
        UnivariateFunction function = new MySimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(InitialGuess.AUTO, MaxEval.MAX_EVAL,
                GoalType.MAXIMIZE);
        optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
    }

    /**
     * Test optimization with a simple function where the minimum is at a
     * point with an extremely small absolute value.
     */
    @Test
    public void testExtremelySmallAbsoluteValue() {
        UnivariateFunction function = new MyExtremelySmallAbsoluteValueFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-308, 1e-308);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(1.366568542495e-308, solution.getPoint(), 1e-309);
    }

    /**
     * Test optimization with a simple function where the minimum is at a
     * point with a negative value.
     */
    @Test
    public void testNegativeValueOptimization() {
        UnivariateFunction function = new MyNegativeValueFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(1.0, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a function that has a non-strictly monotonic
     * behavior.
     */
    @Test
    public void testNonMonotonicFunctionOptimization() {
        UnivariateFunction function = new MyNonMonotonicFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(3.75, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a function that has a very steep slope.
     */
    @Test
    public void testSteepSlopeFunctionOptimization() {
        UnivariateFunction function = new MySteepSlopeFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(1.0, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a function that has a very flat slope.
     */
    @Test
    public void testFlatSlopeFunctionOptimization() {
        UnivariateFunction function = new MyFlatSlopeFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(2.5, solution.getPoint(), 1e-9);
    }

    /**
     * Test optimization with a function that has a very large range.
     */
    @Test
    public void testLargeRangeFunctionOptimization() {
        UnivariateFunction function = new MyLargeRangeFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair solution = optimizer.optimize(function, InitialGuess.AUTO, MaxEval.MAX_EVAL);
        assertEquals(1.1752e-31, solution.getPoint(), 1e-32);
        assertEquals(1.21645e-16, solution.getValue(), 1e-16);
    }

    private static class MySimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * Math.sin(x) - 1;
        }
    }

    private static class MyExtremelySmallAbsoluteValueFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.exp(-Math.abs(x)) * Math.sin(Math.abs(x) * E.PI) * Math.pow(x, 0.1);
        }
    }

    private static class MyNegativeValueFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x - 1.0;
        }
    }

    private static class MyNonMonotonicFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < 0) {
                return x * x * x;
            } else {
                return -x * x * x;
            }
        }
    }

    private static class MySteepSlopeFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.exp(-Math.abs(x - 1.0)) * Math.sin(Math.abs(x - 1.0) * 10.0);
        }
    }

    private static class MyFlatSlopeFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(1 - Math.abs(x - 2.5) / 0.5, 2);
        }
    }

    private static class MyLargeRangeFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.abs(x), 0.1) * Math.sin(Math.abs(x));
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous,
                PointValuePair current) {
            return false;
        }
    }

    private static class MyAlwaysConvergedChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous,
                PointValuePair current) {
            return true;
        }
    }

    private static class MyIterationLimitChecker implements ConvergenceChecker<PointValuePair> {
        private final int limit;

        public MyIterationLimitChecker(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous,
                PointValuePair current) {
            return iteration >= limit;
        }
    }
}