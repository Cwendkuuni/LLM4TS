package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.simple.SimpleFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    /**
     * Test method optimize.
     */
    @Test
    public void testOptimize() {
        MultivariateFunction testFunction = new TestFunction();
        PointValuePair result = optimizer.optimize(testFunction);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(result);
        System.out.println("Minimum found: " + Arrays.toString(result.getPoint()));
        System.out.println("Minimum value: " + result.getValue());
    }

    /**
     * Test method optimize with user-defined GoalType.
     */
    @Test
    public void testOptimizeGoalType() {
        MultivariateFunction testFunction = new TestFunction();
        optimizer = new PowellOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(testFunction);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertNotNull(result);
        System.out.println("Maximum found: " + Arrays.toString(result.getPoint()));
        System.out.println("Maximum value: " + result.getValue());
    }

    /**
     * Test method optimize with custom ConvergenceChecker.
     */
    @Test
    public void testOptimizeCustomChecker() {
        CustomConvergenceChecker customChecker = new CustomConvergenceChecker();
        MultivariateFunction testFunction = new TestFunction();
        optimizer = new PowellOptimizer(1e-6, 1e-6, customChecker);
        PointValuePair result = optimizer.optimize(testFunction);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(result);
        System.out.println("Minimum found: " + Arrays.toString(result.getPoint()));
        System.out.println("Minimum value: " + result.getValue());
    }

    /**
     * Test method optimize with exception.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeException() {
        optimizer = new PowellOptimizer(-1e-6, 1e-6);
        MultivariateFunction testFunction = new TestFunction();
        optimizer.optimize(testFunction);
    }

    /**
     * Test method optimize with not strictly positive absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeAbsThresholdNegative() {
        optimizer = new PowellOptimizer(1e-6, -1e-6);
        MultivariateFunction testFunction = new TestFunction();
        optimizer.optimize(testFunction);
    }

    /**
     * Test method optimize with not strictly positive relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeRelThresholdZero() {
        optimizer = new PowellOptimizer(0, 1e-6);
        MultivariateFunction testFunction = new TestFunction();
        optimizer.optimize(testFunction);
    }

    /**
     * Test line search.
     */
    @Test
    public void testLineSearch() {
        MultivariateFunction testFunction = new TestFunctionLineSearch();
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double alpha) {
                double[] p = new double[]{0, 0};
                p[0] += alpha;
                p[1] += alpha * alpha;
                return testFunction.value(p);
            }
        };
        PointValuePair result = new PowellOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-6, 1e-6)).optimize(f);
        assertEquals(0.5, result.getValue(), 1e-6);
    }

    /**
     * Test line search with convergence checker.
     */
    @Test
    public void testLineSearchCustomChecker() {
        MultivariateFunction testFunction = new TestFunctionLineSearch();
        LineSearchCustomChecker customChecker = new LineSearchCustomChecker();
        PointValuePair result = new PowellOptimizer(1e-6, 1e-6, customChecker).optimize(testFunction);
        assertEquals(0.5, result.getValue(), 1e-6);
    }

    /**
     * Test function for testing Powell Optimizer.
     */
    private static class TestFunction implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(Math.sqrt(Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0))), 2);
        }

        @Override
        public PointValuePair gradient(double[] x) {
            return new PointValuePair(new double[0], 0.0);
        }
    }

    /**
     * Test function for testing line search.
     */
    private static class TestFunctionLineSearch implements MultivariateFunction {
        @Override
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1] + x[0] * x[1] + x[1] * x[0];
        }

        @Override
        public PointValuePair gradient(double[] x) {
            return new PointValuePair(new double[]{2 * x[1] + x[0], 2 * x[0] + x[1]}, 4 * (x[0] + x[1]));
        }
    }

    /**
     * Custom ConvergenceChecker.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double fPrevious = previous.getValue();
            double fCurrent = current.getValue();
            double delta = Math.abs(fPrevious - fCurrent);
            return (iteration > 10 || delta < 1e-8);
        }
    }

    /**
     * Custom ConvergenceChecker for line search.
     */
    private static class LineSearchCustomChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            double fPrevious = previous.getValue();
            double fCurrent = current.getValue();
            double delta = Math.abs(fPrevious - fCurrent);
            return (iteration > 10 || delta < 1e-8);
        }
    }
}