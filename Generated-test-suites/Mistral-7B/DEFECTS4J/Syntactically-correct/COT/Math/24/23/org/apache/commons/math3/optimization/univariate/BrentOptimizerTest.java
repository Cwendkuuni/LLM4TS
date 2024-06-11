package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private UnivariateFunction testFunction1;
    private UnivariateFunction testFunction2;

    @Before
    public void setup() {
        testFunction1 = new TestFunction1();
        testFunction2 = new TestFunction2();
    }

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction1, bounds, GoalType.MINIMIZE);
        assertEquals(-3.0, result.getPoint(), 1e-5);
        assertEquals(3.0, testFunction1.value(result.getPoint()), 1e-5);
    }

    @Test
    public void testBrentOptimizerMinimizationWithSmallThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-9, null);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction1, bounds, GoalType.MINIMIZE);
        assertEquals(-3.0, result.getPoint(), 1e-8);
        assertEquals(3.0, testFunction1.value(result.getPoint()), 1e-8);
    }

    @Test
    public void testBrentOptimizerMinimizationWithConvergenceChecker() {
        MyConvergenceChecker checker = new MyConvergenceChecker(10);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction1, bounds, GoalType.MINIMIZE);
        assertTrue(checker.isConverged());
        assertEquals(-3.0, result.getPoint(), 1e-5);
        assertEquals(3.0, testFunction1.value(result.getPoint()), 1e-5);
    }

    @Test
    public void testBrentOptimizerMinimizationWithWrongArguments() {
        try {
            new BrentOptimizer(-1e-6, 1e-6, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new BrentOptimizer(1e-6, -1e-6, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new BrentOptimizer(1e-6, 1e-6, null, GoalType.MAXIMIZE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction2, bounds, GoalType.MAXIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-2.0, testFunction2.value(result.getPoint()), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximizationWithSmallThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-9, null);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction2, bounds, GoalType.MAXIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-8);
        assertEquals(-2.0, testFunction2.value(result.getPoint()), 1e-8);
    }

    @Test
    public void testBrentOptimizerMaximizationWithConvergenceChecker() {
        MyConvergenceChecker checker = new MyConvergenceChecker(10);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        SimpleBounds bounds = new SimpleBounds(0.0, 10.0);
        PointValuePair result = optimizer.optimize(testFunction2, bounds, GoalType.MAXIMIZE);
        assertTrue(checker.isConverged());
        assertEquals(2.0, result.getPoint(), 1e-5);
        assertEquals(-2.0, testFunction2.value(result.getPoint()), 1e-5);
    }

    @Test
    public void testBrentOptimizerMaximizationWithWrongArguments() {
        try {
            new BrentOptimizer(-1e-6, 1e-6, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new BrentOptimizer(1e-6, -1e-6, null);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        try {
            new BrentOptimizer(1e-6, 1e-6, null, GoalType.MINIMIZE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithBoundsOutOfOrder() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        SimpleBounds bounds = new SimpleBounds(10.0, 0.0);
        try {
            optimizer.optimize(testFunction1, bounds, GoalType.MINIMIZE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerWithFunctionThrowsException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) throws Exception {
                throw new RuntimeException("Function threw an exception");
            }
        };

        try {
            BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
            SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
            optimizer.optimize(function, bounds, GoalType.MINIMIZE);
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    private static class TestFunction1 implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 3 * x * Math.pow(Math.E, x) + 2;
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class TestFunction2 implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + 2;
        }

        @Override
        public double value(double[] x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        private int maxIterations;

        public MyConvergenceChecker(int maxIterations) {
            this.maxIterations = maxIterations;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return (iteration >= maxIterations);
        }
    }
}