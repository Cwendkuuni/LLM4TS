package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.junit.Assert;

/**
 * Test for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /**
     * Test for simplex optimizer when the goal is to minimize.
     */
    @org.junit.Test
    public void testMinimize() {
        // Objective function to be optimized.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Test with a simplex of size 2.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(new SimpleSimplex(2));

        double[] startPoint = new double[] { 1.0, 2.0 };
        PointValuePair result = optimizer.optimize(1000, f, GoalType.MINIMIZE, startPoint);

        // Verify the result.
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test for simplex optimizer when the goal is to maximize.
     */
    @org.junit.Test
    public void testMaximize() {
        // Objective function to be optimized.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Test with a simplex of size 2.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(new SimpleSimplex(2));

        double[] startPoint = new double[] { 1.0, 2.0 };
        PointValuePair result = optimizer.optimize(1000, f, GoalType.MAXIMIZE, startPoint);

        // Verify the result.
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }, result.getPoint(), 1e-6);
    }

    /**
     * Test for simplex optimizer when the optimization data contains a simplex.
     */
    @org.junit.Test
    public void testOptimizationData() {
        // Objective function to be optimized.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Test with a simplex of size 2.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        double[] startPoint = new double[] { 1.0, 2.0 };
        SimpleSimplex simplex = new SimpleSimplex(2);
        simplex.build(startPoint);

        PointValuePair result = optimizer.optimize(1000, f, GoalType.MINIMIZE, new OptimizationData() {
            @Override
            public AbstractSimplex getSimplex() {
                return simplex;
            }
        });

        // Verify the result.
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getPoint());
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] { 0.0, 0.0 }, result.getPoint(), 1e-6);
    }

    /**
     * Test for simplex optimizer when an invalid optimization data is provided.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        // Objective function to be optimized.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        // Test with an empty optimization data.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.optimize(1000, f, GoalType.MINIMIZE, null);
    }
}