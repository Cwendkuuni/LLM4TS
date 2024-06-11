package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    private MultivariateFunction function;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        function = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 2.0, 2.0) + Math.pow(point[1] - 2.0, 2.0);
            }
        };
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
    }

    /**
     * Test optimization in 2 dimensions.
     */
    @Test
    public void testTwoDimensions() {
        double[] startPoint = {3.0, 3.0};
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[] {2.0, 2.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization in 3 dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testThreeDimensions() {
        double[] startPoint = {3.0, 3.0, 3.0};
        optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
    }

    /**
     * Test optimization with null function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        optimizer.optimize(100, null, GoalType.MINIMIZE, new double[0]);
    }

    /**
     * Test optimization with null start point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullStartPoint() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, null);
    }

    /**
     * Test optimization with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer.optimize(100, function, GoalType.MAXIMIZE, new double[0]);
    }

    /**
     * Test optimization with no evaluations allowed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoEvaluations() {
        optimizer.optimize(0, function, GoalType.MINIMIZE, new double[0]);
    }

    /**
     * Test optimization with simplex not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnsetSimplex() {
        optimizer.optimize(100, function, GoalType.MINIMIZE, new double[0]);
    }
}