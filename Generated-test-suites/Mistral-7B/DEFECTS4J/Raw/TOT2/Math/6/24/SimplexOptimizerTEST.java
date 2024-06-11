package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SimplexOptimizer.
 */
public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     */
    @Test
    public void testOptimize() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };

        double[] startPoint = {3.0, 3.0};
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        Assert.assertEquals(2.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     * With wrong number of arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullArgumentException() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };

        optimizer.optimize(100, function, GoalType.MINIMIZE, null);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, double[])}.
     * With wrong number of dimensions in optimization data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIllegalArgumentException() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };

        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[])}.
     */
    @Test
    public void testOptimizeWithOptimizationData() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };

        double[] startPoint = {3.0, 3.0};
        AbstractSimplex simplex = new SimpleSimplex(2);
        OptimizationData data = new OptimizationData();
        data.setParameter("simplex", simplex);

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, data, startPoint);

        Assert.assertEquals(2.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 1e-6);
    }

    /**
     * Test method for {@link SimplexOptimizer#optimize(int, MultivariateFunction, GoalType, OptimizationData[], double[])}.
     */
    @Test
    public void testOptimizeWithOptimizationDataAndStartPoint() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };

        double[] startPoint = {3.0, 3.0};
        AbstractSimplex simplex = new SimpleSimplex(2);
        OptimizationData data = new OptimizationData();
        data.setParameter("simplex", simplex);

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, data, startPoint);

        Assert.assertEquals(2.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.5, 1.5}, result.getPoint(), 1e-6);
    }
}