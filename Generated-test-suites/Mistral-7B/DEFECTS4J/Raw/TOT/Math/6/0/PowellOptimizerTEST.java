package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.simple.SphereFunction;
import org.apache.commons.math3.optim.nonlinear.vector.LinearConstrainedMultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimizing a simple function with Powell's method.
     */
    @Test
    public void testSimpleFunction() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final UnivariateFunction f = new SphereFunction(0.1);
        final double[] x = {1.0, 2.0};
        final PointValuePair result = optimizer.optimize(f, x);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimizing a function with a local minimum and a global minimum
     * with Powell's method.
     */
    @Test
    public void testFunctionWithLocalAndGlobalMinima() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final UnivariateFunction f = new SphereFunction(1.0);
        final double[] x = {2.0, 2.0};
        final PointValuePair result = optimizer.optimize(f, x);
        assertEquals(1.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimizing a function with bounds using Powell's method.
     */
    @Test
    public void testFunctionWithBounds() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final UnivariateFunction f = new SphereFunction(2.5);
        final double[] bounds = {0.0, 5.0};
        final PointValuePair result = optimizer.optimize(f, bounds);
        assertEquals(2.5, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{2.5, 2.5}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimizing a function with linear constraints using Powell's method.
     */
    @Test
    public void testFunctionWithLinearConstraints() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final LinearConstrainedMultivariateFunction f = new LinearConstrainedMultivariateFunction(
                new double[][]{
                    {1, 1, -1},
                    {-1, 1, -1},
                    {1, -1, -1}
                },
                new double[]{0.0, 0.0, 0.0},
                new double[]{0.0, 0.0, 0.0},
                new double[]{2.0, 2.0, 2.0});
        final LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1, 1}, new double[]{1, 1}, 'L'),
                new LinearConstraint(new double[]{-1, 1}, new double[]{1, 1}, 'L'),
                new LinearConstraint(new double[]{1, -1}, new double[]{1, 1}, 'L')
        };
        final double[] bounds = {0.0, 0.0, 0.0};
        final PointValuePair result = optimizer.optimize(f, constraints, bounds);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimizing a function with unreachable minimum.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testFunctionWithUnreachableMinimum() {
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        final UnivariateFunction f = new SphereFunction(-1.0);
        final double[] x = {0.0, 0.0};
        optimizer.optimize(f, x);
    }
}