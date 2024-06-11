package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.NonUnaryOperator;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.SimpleBounds;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    /**
     * Test optimization of a function with a known minimum.
     */
    @Test
    public void testOptimization() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);

        assertEquals(0.5, result.getPoint(), 1e-7);
        assertEquals(-0.25, result.getValue(), 1e-7);
    }

    /**
     * Test optimization of a function with a known maximum.
     */
    @Test
    public void testOptimizationMaximum() {
        UnivariateFunction function = new SquareFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MAXIMUM);

        assertEquals(10.0, result.getPoint(), 1e-7);
        assertEquals(100.0, result.getValue(), 1e-7);
    }

    /**
     * Test optimization of a function with a known inflection point.
     */
    @Test
    public void testOptimizationInflection() {
        UnivariateFunction function = new InflectionFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        PointValuePair result = optimizer.optimize(function, new SimpleBounds(-10.0, 10.0), GoalType.MINIMIZE);

        assertEquals(0.0, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    /**
     * Test optimization of a function that returns infinity at a bound.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationBound() {
        UnivariateFunction function = new InfinityFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with an invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInvalidGoalType() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MAXIMUM);
    }

    /**
     * Test optimization with a null function.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(null, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with a null bounds.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationNullBounds() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, null, GoalType.MINIMIZE);
    }

    /**
     * Test optimization with a function that doesn't implement {@link #value(double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizationUnsupportedOperation() {
        UnivariateFunction function = new UnsupportedFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with a function that doesn't implement {@link #value(double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationThrowsException() {
        UnivariateFunction function = new ThrowsExceptionFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
            fail();
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test optimization with a non-unary function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationNonUnaryFunction() {
        UnivariateFunction function = new NonUnaryFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with an initial guess outside the bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInitialGuessOutsideBounds() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE, new InitialGuess(-20.0));
    }

    /**
     * Test optimization with a relative tolerance less than machine epsilon.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationSmallRelativeTolerance() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-16, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with an absolute tolerance less than zero.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationNegativeAbsoluteTolerance() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Test optimization with a convergence checker.
     */
    @Test
    public void testOptimizationWithConvergenceChecker() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new SimpleConvergenceChecker(10));

        PointValuePair result = optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);

        assertEquals(0.5, result.getPoint(), 1e-7);
        assertEquals(-0.25, result.getValue(), 1e-7);
        assertTrue(optimizer.hasConverged());
    }

    /**
     * Test optimization with a non-strictly positive absolute tolerance.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationNonPositiveAbsoluteTolerance() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    /**
     * Test optimization with a non-strictly positive relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationNonPositiveRelativeTolerance() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-6);

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
            fail();
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test optimization with a convergence checker that always fails.
     */
    @Test
    public void testOptimizationWithFailingConvergenceChecker() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new FailingConvergenceChecker());

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
            fail();
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test optimization with a convergence checker that never converges.
     */
    @Test
    public void testOptimizationWithNeverConvergingConvergenceChecker() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new NeverConvergingConvergenceChecker());

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
            fail();
        } catch (Exception e) {
            // Expected
        }
    }

    /**
     * Test optimization with a null convergence checker.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizationNullConvergenceChecker() {
        UnivariateFunction function = new AbsFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);

        optimizer.optimize(function, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
    }

    /**
     * Absolute function for testing.
     */
    static class AbsFunction implements UnivariateFunction {

        public double value(double x) {
            return Math.abs(x);
        }
    }

    /**
     * Square function for testing.
     */
    static class SquareFunction implements UnivariateFunction {

        public double value(double x) {
            return x * x;
        }
    }

    /**
     * Inflection function for testing.
     */
    static class InflectionFunction implements UnivariateFunction {

        public double value(double x) {
            return Math.pow(Math.abs(x), 3) - 0.5 * Math.pow(Math.abs(x), 2);
        }
    }

    /**
     * Function that always returns infinity.
     */
    static class InfinityFunction implements UnivariateFunction {

        public double value(double x) {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Function that doesn't implement {@link #value(double)}.
     */
    static class UnsupportedFunction implements UnivariateFunction {

        public double value(double x) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Function that throws an exception.
     */
    static class ThrowsExceptionFunction implements UnivariateFunction {

        @Override
        public double value(double x) throws Exception {
            throw new Exception();
        }
    }

    /**
     * Non-unary function for testing.
     */
    static class NonUnaryFunction implements UnivariateFunction {

        public double value(double x, double y) {
            throw new IllegalArgumentException("Not unary function");
        }

        public double value(double x) {
            return Math.sin(x);
        }
    }

    /**
     * Convergence checker that always returns true.
     */
    static class SimpleConvergenceChecker implements ConvergenceChecker<PointsValuePair> {

        private final int maxIterations;

        public SimpleConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        public boolean converged(int iteration, PointsValuePair previous, PointsValuePair current) {
            return iteration >= maxIterations;
        }
    }

    /**
     * Convergence checker that always fails.
     */
    static class FailingConvergenceChecker implements ConvergenceChecker<PointsValuePair> {

        @Override
        public boolean converged(int iteration, PointsValuePair previous, PointsValuePair current) {
            throw new RuntimeException("Failed to converge");
        }
    }

    /**
     * Convergence checker that never converges.
     */
    static class NeverConvergingConvergenceChecker implements ConvergenceChecker<PointsValuePair> {

        @Override
        public boolean converged(int iteration, PointsValuePair previous, PointsValuePair current) {
            return false;
        }
    }
}