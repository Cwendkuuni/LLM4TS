package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.simple.ScaledSinCosFunction;
import org.apache.commons.math3.optimization.simple.SquaredErrorFunction;

import org.junit.Test;

import java.util.Arrays;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        // Test empty constructor
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNull(optimizer.getMin());
        assertNull(optimizer.getMax());
        assertNull(optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertNull(optimizer.doOptimize());

        // Test constructor with no arguments
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 0.001);

        // Test constructor with convergence checker
        optimizer = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(1e-5, optimizer.getAbsoluteThreshold(), 0.001);
        assertNotNull(optimizer.getConvergenceChecker());

        // Test optimization with a simple function (Squared Error)
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        Function function = new SquaredErrorFunction(2.0);
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(4.0, result.getValue(), 0.001);

        // Test optimization with a simple function (Scaled Sin-Cos)
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.setMin(-Math.PI);
        optimizer.setMax(Math.PI);
        function = new ScaledSinCosFunction(1.0);
        result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);

        // Test optimization with unbounded interval
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        optimizer.setMin(Double.NEGATIVE_INFINITY);
        optimizer.setMax(Double.POSITIVE_INFINITY);
        function = new SquaredErrorFunction(0.0);
        result = optimizer.optimize(function);
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        MyConvergenceChecker convergenceChecker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, convergenceChecker);

        // Test optimization with a simple function
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        Function function = new SquaredErrorFunction(2.0);
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(-2.0, result.getPoint(), 0.001);
        assertEquals(4.0, result.getValue(), 0.001);
        assertTrue(convergenceChecker.isConverged());

        // Test optimization with a simple function that does not converge
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        function = new FunctionWithBadBehavior();
        result = optimizer.optimize(function);
        assertEquals(-2.3, result.getPoint(), 0.001); // arbitrary value approaching minim
        assertFalse(convergenceChecker.isConverged());
    }

    @Test
    public void testBrentOptimizerWithBadInput() {
        // Test optimization with negative absolute threshold
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-5);
        try {
            optimizer.optimize(new SquaredErrorFunction(0.0));
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test optimization with zero relative threshold
        optimizer = new BrentOptimizer(0.0, 1e-5);
        try {
            optimizer.optimize(new SquaredErrorFunction(0.0));
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        // Test optimization with null convergence checker
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-5, 1e-5, null);
        try {
            optimizer2.optimize(new SquaredErrorFunction(0.0));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test optimization with null function
        BrentOptimizer optimizer3 = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer3.optimize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test helper class for convergence checker
    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        private boolean converged;

        @Override
        public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            converged = true;
            return true;
        }

        public boolean isConverged() {
            boolean result = converged;
            converged = false;
            return result;
        }
    }

    // Test helper class for function with bad behavior
    private static class FunctionWithBadBehavior implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < 0.0) {
                return -(x * x) / 10.0;
            } else {
                return x * x;
            }
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] gradient(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] hessian(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double[] hessian(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }

        @Override
        public String getName() {
            return "BadFunction";
        }
    }
}