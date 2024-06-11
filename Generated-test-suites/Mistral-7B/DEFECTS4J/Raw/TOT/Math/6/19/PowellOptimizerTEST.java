package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization with a simple quadratic function.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test
    public void testQuadraticFunction() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += x[i] * x[i] * x[i];
                    }
                    return sum;
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE,
                                                   new double[]{1., 1.});
        assertEquals(0., result.getValue()[0], 1e-8);
        assertEquals(0., result.getValue()[1], 1e-8);
        assertEquals(1., result.getPoint()[0], 1e-8);
        assertEquals(1., result.getPoint()[1], 1e-8);
    }

    /**
     * Test optimization with a simple function having multiple local minima.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test
    public void testFunctionWithMultipleLocalMinima() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += Math.exp(-x[i]);
                    }
                    return sum;
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE,
                                                   new double[]{1., 1.});
        assertEquals(Math.E * (Math.E - 1), result.getValue()[0], 1e-5);
        assertEquals(Math.E * (Math.E - 1), result.getValue()[1], 1e-5);
        assertEquals(1., result.getPoint()[0], 1e-5);
        assertEquals(1., result.getPoint()[1], 1e-5);
    }

    /**
     * Test optimization with a non-differentiable function.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test
    public void testNonDifferentiableFunction() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    double sum = 0;
                    for (int i = 0; i < x.length; i++) {
                        sum += Math.abs(x[i] - 1);
                    }
                    return sum;
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE,
                                                   new double[]{2., 2.});
        assertEquals(4., result.getValue()[0], 1e-5);
        assertEquals(4., result.getValue()[1], 1e-5);
        assertEquals(1., result.getPoint()[0], 1e-5);
        assertEquals(1., result.getPoint()[1], 1e-5);
    }

    /**
     * Test optimization with a non-convex function.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNonConvexFunction() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    if (x[0] > x[1]) {
                        return -Math.pow(x[0] - x[1], 2);
                    } else {
                        return Math.pow(x[1] - x[0], 2);
                    }
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, new double[]{1., 1.});
    }

    /**
     * Test optimization with a non-bounded function.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNonBoundedFunction() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    return 1 / x[0];
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(f, GoalType.MINIMIZE, new double[]{1.});
    }

    /**
     * Test optimization with a non-positive starting point.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Test
    public void testNonPositiveStartingPoint() throws NumberIsTooSmallException {
        MultivariateFunction f = new MultivariateFunction() {
                public double value(double[] x) {
                    return -Math.pow(x[0], 2);
                }
            };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE,
                                                   new double[]{-1.});
        assertEquals(-1., result.getValue()[0], 1e-8);
        assertEquals(-1., result.getValue()[1], 1e-8);
        assertEquals(-1., result.getPoint()[0], 1e-8);
        assertEquals(-1., result.getPoint()[1], 1e-8);
    }
}