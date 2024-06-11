package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 3, 4 };
        PointValuePair result = optimizer.optimize(f, x);
        assertEquals(new PointValuePair(new double[] { 0, 0 }, 0), result);
    }

    /**
     * Test optimization with a noisy quadratic function.
     */
    @Test
    public void testQuadraticFunctionNoisy() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + Math.random();
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 3, 4 };
        PointValuePair result = optimizer.optimize(f, x);
        // The noisy optimization may return a different point but the value
        // should be close to the optimal one.
        assertEquals(new PointValuePair(new double[] { 0, 0 }, 0.01, 0.001), result, 0.01);
    }

    /**
     * Test optimization with a non-differentiable function.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testNonDifferentiableFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 0) {
                    return Math.log(Math.abs(x[0])) * Math.sin(1 / x[0]);
                } else {
                    throw new NumberIsTooSmallException(x[0], 0.0);
                }
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { -1 };
        optimizer.optimize(f, x);
    }

    /**
     * Test optimization with a discontinuous function.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDiscontinuousFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 0) {
                    return Math.pow(x[0], 2);
                } else {
                    throw new NumberIsTooSmallException(x[0], 0.0);
                }
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { -1 };
        optimizer.optimize(f, x);
    }

    /**
     * Test optimization with a function that has no solution in the domain.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoSolutionFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.log(x[0] - 1);
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 0 };
        optimizer.optimize(f, x);
    }

    /**
     * Test optimization with a function that has multiple solutions.
     */
    @Test
    public void testMultipleSolutionFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1, 2) + Math.pow(x[1] - 1, 2);
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 1, 1 };
        PointValuePair result = optimizer.optimize(f, x);
        // The Powell optimizer may return one of the solutions.
        assertEquals(new PointValuePair(new double[] { 1, 1 }, 2), result, 0.01);
    }

    /**
     * Test optimization with a function that has no local minimum.
     */
    @Test
    public void testNoLocalMinimumFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.abs(x[0] - 1) + Math.abs(x[1] - 1);
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 0, 0 };
        PointValuePair result = optimizer.optimize(f, x);
        // The Powell optimizer may not find any minimum.
        assertNotNull(result);
    }

    /**
     * Test optimization with a function that has a solution at the boundary.
     */
    @Test
    public void testBoundarySolutionFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.max(x[0], 0) - 1, 2) + Math.pow(x[1], 2);
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return (iter > 10);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5, checker);
        double[] x = new double[] { 0, 0 };
        PointValuePair result = optimizer.optimize(f, x);
        // The Powell optimizer may return the boundary solution.
        assertEquals(new PointValuePair(new double[] { 0, 0 }, 1), result, 0.01);
    }
}