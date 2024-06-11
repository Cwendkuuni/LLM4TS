package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test for PowellOptimizer constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test for PowellOptimizer constructor with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceCheckerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, new MyConvergenceChecker());
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test for PowellOptimizer constructor with custom relative threshold,
     * absolute threshold, and line search tolerances.
     */
    @Test
    public void testCustomThresholdsConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, 0.0001, 0.001, new SimpleUnivariateValueChecker(0.001, 0.01));
        Assert.assertNotNull(optimizer);
    }

    /**
     * Test optimization with a simple function.
     */
    @Test
    public void testOptimizationSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        double[] startPoint = {2.0, 2.0};
        PointValuePair solution = optimizer.optimize(f, startPoint);
        Assert.assertEquals(0.0, solution.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization with a function with multiple local minima.
     */
    @Test
    public void testOptimizationMultipleLocalMinima() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) * Math.cos(x[1]), 2)
                       + Math.pow(Math.cos(x[0]) * Math.sin(x[1]), 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        double[] startPoint = {Math.PI / 4, Math.PI / 4};
        PointValuePair solution = optimizer.optimize(f, startPoint);
        Assert.assertEquals(0.25, solution.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{-Math.PI / 4, Math.PI / 4}, solution.getPoint(), 1e-5);
    }

    /**
     * Test optimization with a function having no solution.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizationNoSolutionFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI) / x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        double[] startPoint = {0.1};
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization with a non-differentiable function.
     */
    @Test
    public void testOptimizationNonDifferentiableFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.abs(x[0] * Math.sin(1 / x[0]));
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        double[] startPoint = {1.0};
        PointValuePair solution = optimizer.optimize(f, startPoint);
        Assert.assertEquals(0.0, solution.getValue(), 1e-5);
        Assert.assertEquals(Double.POSITIVE_INFINITY, solution.getPoint()[0], 1e-5);
    }

    /**
     * Test optimization with a function with negative input.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizationFunctionWithNegativeInput() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        double[] startPoint = {-1.0};
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization with a function with absolute tolerance too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationFunctionWithAbsoluteToleranceTooSmall() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        double[] startPoint = {2.0};
        optimizer.optimize(f, startPoint);
    }

    /**
     * Test optimization with a function with relative tolerance too small.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizationFunctionWithRelativeToleranceTooSmall() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0, 0.0001);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        double[] startPoint = {2.0};
        optimizer.optimize(f, startPoint);
    }
}

class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
        // Custom convergence checker condition
        return current.getValue() < previous.getValue() * 0.1;
    }
}