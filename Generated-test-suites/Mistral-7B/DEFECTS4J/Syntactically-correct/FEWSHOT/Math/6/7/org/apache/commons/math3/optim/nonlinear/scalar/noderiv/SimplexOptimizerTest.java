package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.test.AbstractMultivariateOptimizerTest;
import org.apache.commons.math3.optimization.test.TestFunctions;
import org.junit.Test;

/**
 * Test cases for SimplexOptimizer.
 */
public class SimplexOptimizerTest extends AbstractMultivariateOptimizerTest {

    /**
     * Test optimizing a function with a simplex.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        MultivariateFunction f = TestFunctions.ROSENBROCK;
        double[] startPoint = {2.0, 2.0};
        GoalType goalType = GoalType.MINIMIZE;

        AbstractSimplex simplex = new NelsonSeibelSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.optimize(10_000, f, goalType, startPoint);

        assertEquals(GoalType.MINIMIZE, goalType);
        assertArrayEquals(new double[] {1.0, 2.9}, result.getPoint(), 0.001);
        assertEquals(-3.6028745332302757E7d, result.getValue(), 1.0E-8d);
    }

    /**
     * Test optimizing a function with a different number of dimensions.
     */
    @Test
    public void testOptimizeFunctionWithDifferentNumberOfDimensions() {
        MultivariateFunction f = TestFunctions.SIN;
        double[] startPoint = {Math.PI / 2.0, Math.PI / 2.0};
        GoalType goalType = GoalType.MINIMIZE;

        AbstractSimplex simplex = new AmaldiSimplex(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.optimize(10_000, f, goalType, startPoint);

        assertEquals(GoalType.MINIMIZE, goalType);
        assertArrayEquals(new double[] {Math.PI / 2.0, Math.PI / 2.0}, result.getPoint(), 0.001);
        assertEquals(0.0d, result.getValue(), 1.0E-6d);
    }

    /**
     * Test optimizing a function with a null simplex.
     */
    @Test(expected = NullPointerException.class)
    public void testOptimizeWithNullSimplex() {
        MultivariateFunction f = TestFunctions.ROSENBROCK;
        double[] startPoint = {2.0, 2.0};
        GoalType goalType = GoalType.MINIMIZE;

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.optimize(10_000, f, goalType, startPoint);
    }
}