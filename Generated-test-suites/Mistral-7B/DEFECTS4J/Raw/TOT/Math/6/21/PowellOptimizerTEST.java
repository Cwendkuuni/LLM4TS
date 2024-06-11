package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;
import org.junit.Assert;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Test the optimization of a simple quadratic function.
     */
    @Test
    public void testOptimizationSimpleQuadraticFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }
        };

        PointValuePair solution = new PowellOptimizer(1e-6, 1e-6).optimize(f);
        Assert.assertEquals(0, solution.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{}, solution.getPoint());
    }

    /**
     * Test the optimization of a non-quadratic function with multiple local
     * minima.
     */
    @Test
    public void testOptimizationNonQuadraticFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(Math.sin(x[i]), 2) / (1 + Math.pow(x[i], 2));
                }
                return sum;
            }
        };

        PointValuePair solution = new PowellOptimizer(1e-6, 1e-6).optimize(f);
        Assert.assertEquals(-1.27532844, solution.getValue(), 1e-8);
        Assert.assertArrayEquals(new double[]{-1.57079633, -3.14159265}, solution.getPoint());
    }

    /**
     * Test the optimization of a function with non-convex domain.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationNonConvexFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 2) {
                    throw new IllegalArgumentException("Non-convex domain");
                }
                return Math.pow(x[0], 2);
            }
        };

        new PowellOptimizer(1e-6, 1e-6).optimize(f);
    }
}