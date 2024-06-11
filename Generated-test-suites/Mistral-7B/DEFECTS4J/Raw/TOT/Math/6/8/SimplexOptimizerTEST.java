package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimpleXPoint;
import org.apache.commons.math3.optim.SimpleXSimplex;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /** Test function for optimization. */
    private static final MultivariateFunction TEST_FUNCTION = new MultivariateFunction() {
        public double value(double[] x) {
            return Math.pow(Math.sin(Math.sqrt(Math.pow(x[0], 2) + Math.pow(x[1], 2))), 2);
        }
    };

    /** Test goal type. */
    private static final GoalType GOAL_TYPE = GoalType.MINIMIZE;

    /** Test simplex size. */
    private static final int SIMPLEX_SIZE = 4;

    /** Test tolerance. */
    private static final double TOLERANCE = 1e-8;

    /** Test maximum iterations. */
    private static final int MAX_ITERATIONS = 1000;

    /** Test convergence checker. */
    private static final SimpleValueChecker CHECKER = new SimpleValueChecker(TOLERANCE);

    /** Test optimization data. */
    private static OptimizationData createOptimizationData() {
        final SimpleXSimplex simplex = new SimpleXSimplex(SIMPLEX_SIZE);
        final double[] startPoint = new double[]{1.0, 1.0};
        simplex.setStartingPoint(startPoint);
        return new OptimizationData() {
            @Override
            public Object getParameter() {
                return simplex;
            }
        };
    }

    /** Test optimization. */
    @Test
    public void testOptimize() {
        // Test optimization with provided simplex.
        final OptimizationData optimizationData = createOptimizationData();

        final SimplexOptimizer optimizer = new SimplexOptimizer(CHECKER);
        final PointValuePair result = optimizer.optimize(MAX_ITERATIONS, TEST_FUNCTION, GOAL_TYPE, optimizationData);

        // Check result.
        final double expected = 0.0;
        Assert.assertEquals(expected, result.getValue(), TOLERANCE);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), TOLERANCE);
    }

    /** Test optimization without simplex. */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithoutSimplex() {
        // Test optimization without simplex.
        final SimplexOptimizer optimizer = new SimplexOptimizer(CHECKER);
        optimizer.optimize(MAX_ITERATIONS, TEST_FUNCTION, GOAL_TYPE, null);
    }

    /** Test optimization with null function. */
    @Test(expected = NullPointerException.class)
    public void testOptimizeWithNullFunction() {
        // Test optimization with null function.
        final SimplexOptimizer optimizer = new SimplexOptimizer(CHECKER);
        optimizer.optimize(MAX_ITERATIONS, null, GOAL_TYPE, createOptimizationData());
    }
}