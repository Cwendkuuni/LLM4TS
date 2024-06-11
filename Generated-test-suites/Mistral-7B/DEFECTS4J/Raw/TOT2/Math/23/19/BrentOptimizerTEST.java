package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        double functionValue = optimizer.optimize(new TestFunction(), -5, 5);
        assertEquals(-2.0, functionValue, 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        double initialGuess = 0.5;
        double functionValue = optimizer.optimize(new TestFunction(), initialGuess, 5);
        assertEquals(-2.0, functionValue, 1e-6);
    }

    @Test
    public void testBrentOptimizerMinimizationWithUserConvergenceChecker() {
        MyConvergenceChecker convergenceChecker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, convergenceChecker);
        double functionValue = optimizer.optimize(new TestFunction(), -5, 5);
        assertEquals(-2.0, functionValue, 1e-6);
        assertTrue(convergenceChecker.hasConverged());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(-1e-5, 1e-5, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0, 1e-5, null);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        double functionValue = optimizer.optimize(new TestFunction(), -5, 5);
        assertEquals(2.0, functionValue, 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizationWithInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE, null);
        double initialGuess = 0.5;
        double functionValue = optimizer.optimize(new TestFunction(), initialGuess, 5);
        assertEquals(2.0, functionValue, 1e-6);
    }

    @Test
    public void testBrentOptimizerMaximizationWithUserConvergenceChecker() {
        MyConvergenceChecker convergenceChecker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE, convergenceChecker);
        double functionValue = optimizer.optimize(new TestFunction(), -5, 5);
        assertEquals(2.0, functionValue, 1e-6);
        assertTrue(convergenceChecker.hasConverged());
    }

    @Test
    public void testBrentOptimizerWithLargeInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        double functionValue = optimizer.optimize(new TestFunctionWithLargeInterval(), -1000, 1000);
        assertEquals(0.5, functionValue, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithLargeIntervalAndInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        double initialGuess = 1.0;
        double functionValue = optimizer.optimize(new TestFunctionWithLargeInterval(), initialGuess, 1000);
        assertEquals(0.5, functionValue, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithLargeIntervalAndUserConvergenceChecker() {
        MyConvergenceChecker convergenceChecker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null, convergenceChecker);
        double functionValue = optimizer.optimize(new TestFunctionWithLargeInterval(), -1000, 1000);
        assertEquals(0.5, functionValue, 1e-5);
        assertTrue(convergenceChecker.hasConverged());
    }

    @Test
    public void testBrentOptimizerWithLargeFunctionValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        double functionValue = optimizer.optimize(new TestFunctionWithLargeFunctionValues(), -10, 10);
        assertEquals(1e10, functionValue, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithLargeFunctionValuesAndInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        double initialGuess = 0.5;
        double functionValue = optimizer.optimize(new TestFunctionWithLargeFunctionValues(), initialGuess, 10);
        assertEquals(1e10, functionValue, 1e-5);
    }

    @Test
    public void testBrentOptimizerWithLargeFunctionValuesAndUserConvergenceChecker() {
        MyConvergenceChecker convergenceChecker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null, convergenceChecker);
        double functionValue = optimizer.optimize(new TestFunctionWithLargeFunctionValues(), -10, 10);
        assertEquals(1e10, functionValue, 1e-5);
        assertTrue(convergenceChecker.hasConverged());
    }

    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2;
        }
    }

    private static class TestFunctionWithLargeInterval implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < -1000 || x > 1000) {
                throw new UnsupportedOperationException();
            }
            return Math.pow(x, 2) + Math.sin(x);
        }
    }

    private static class TestFunctionWithLargeFunctionValues implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(100, x);
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        private boolean hasConverged = false;

        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            hasConverged = true;
            return current.getValue() <= previous.getValue();
        }

        public boolean hasConverged() {
            return hasConverged;
        }
    }
}