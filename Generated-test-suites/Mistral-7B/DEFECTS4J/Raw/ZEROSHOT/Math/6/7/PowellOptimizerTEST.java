package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test the Powell optimizer with a simple quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test the Powell optimizer with a simple quadratic function and custom
     * convergence checker.
     */
    @Test
    public void testQuadraticFunctionWithCustomChecker() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }
        };

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                for (int i = 0; i < current.getPoint().length; i++) {
                    if (Math.abs(current.getPoint()[i] - previous.getPoint()[i]) > 1e-6) {
                        return false;
                    }
                }
                return true;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test the Powell optimizer with a simple quadratic function and a small
     * tolerance.
     */
    @Test
    public void testQuadraticFunctionWithSmallTolerance() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-9, 1e-9);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(0, result.getPoint()[0], 1e-9);
        Assert.assertEquals(0, result.getPoint()[1], 1e-9);
        Assert.assertEquals(0, result.getValue(), 1e-9);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function.
     */
    @Test
    public void testRosenbrockFunction() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(1, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and custom
     * convergence checker.
     */
    @Test
    public void testRosenbrockFunctionWithCustomChecker() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                for (int i = 0; i < current.getPoint().length; i++) {
                    if (Math.abs(current.getPoint()[i] - previous.getPoint()[i]) > 1e-6) {
                        return false;
                    }
                }
                return true;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(1, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and a small tolerance.
     */
    @Test
    public void testRosenbrockFunctionWithSmallTolerance() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-9, 1e-9);
        PointValuePair result = optimizer.optimize(function);

        Assert.assertEquals(1, result.getPoint()[0], 1e-9);
        Assert.assertEquals(1, result.getPoint()[1], 1e-9);
        Assert.assertEquals(0, result.getValue(), 1e-9);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and the default
     * maximum number of iterations.
     */
    @Test
    public void testRosenbrockFunctionMaxIterations() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, Integer.MAX_VALUE);

        Assert.assertEquals(1, result.getPoint()[0], 1e-6);
        Assert.assertEquals(1, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0, result.getValue(), 1e-6);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and a negative tolerance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRosenbrockFunctionNegativeTolerance() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(-1e-6, 1e-6);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and a tolerance
     * smaller than the machine epsilon.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testRosenbrockFunctionSmallTolerance() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-16, 1e-6);
        optimizer.optimize(function);
    }

    /**
     * Test the Powell optimizer with a non-quadratic function and a negative
     * absolute tolerance.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRosenbrockFunctionNegativeAbsoluteTolerance() {
        final MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length - 1; i++) {
                    double term = 100 * (Math.pow(x[i + 1] - Math.pow(x[i], 2), 2)
                            + Math.pow(1 - x[i], 2));
                    sum += term;
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, -1e-6);
    }
}