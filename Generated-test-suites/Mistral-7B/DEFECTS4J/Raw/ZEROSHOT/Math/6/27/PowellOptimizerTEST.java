package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test function f(x) = x[0]^2 + x[1]^2.
     */
    @Test
    public void testFunctionSimple() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        double[] startPoint = {2.0, 3.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test function with local minimum.
     */
    @Test
    public void testFunctionLocalMinimum() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double value = 0.0;
                for (int i = 0; i < x.length; i++) {
                    value += Math.pow(x[i] - 2, 2);
                }
                return value;
            }
        };

        double[] startPoint = {4.0, 4.0, 4.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(12.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{2.0, 2.0, 2.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test function with multiple local minimum.
     */
    @Test
    public void testFunctionMultipleLocalMinimum() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double value = 0.0;
                for (int i = 0; i < x.length; i++) {
                    value += Math.pow(x[i] - 1, 2);
                }
                return value;
            }
        };

        double[] startPoint = {-2.0, -2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(4.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.0, -1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test function with non-convex valley.
     */
    @Test(expected = org.apache.commons.math3.exception.GoalTypeMismatchedException.class)
    public void testFunctionNonConvexValley() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 0) {
                    return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
                } else {
                    return -Math.pow(x[0] + 1, 2) + Math.pow(x[1] - 1, 2);
                }
            }
        };

        double[] startPoint = {-2.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        fail("Expected GoalTypeMismatchedException");
    }

    /**
     * Test function with goal type MAXIMIZE.
     */
    @Test
    public void testFunctionMaximize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -x[0] * x[0] - x[1] * x[1];
            }
        };

        double[] startPoint = {2.0, 3.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(0.0, -result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test with custom convergence checker.
     */
    @Test
    public void testFunctionCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-4, 1e-4,
                new CustomConvergenceChecker());

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
            }
        };

        double[] startPoint = {2.0, 3.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(4.0, result.getValue(), 1e-4);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-4);
    }

    /**
     * Custom convergence checker.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        /**
         * Check for convergence.
         * @param iteration Iteration number.
         * @param previous Previous optimization result.
         * @param current Current optimization result.
         * @return true if convergence is achieved.
         */
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double relativeDifference = Math.abs(previous.getValue() - current.getValue()) / Math.max(1.0, Math.abs(previous.getValue()));
            return relativeDifference < 1e-4;
        }
    }
}