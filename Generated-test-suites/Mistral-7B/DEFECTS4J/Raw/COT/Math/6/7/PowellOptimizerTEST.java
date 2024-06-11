package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.MaxEval;
import static org.apache.commons.math3.optim.minimization.SimpleBounds.bounds;

/**
 * Tests for PowellOptimizer class.
 */
public class PowellOptimizerTest {
    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}.
     */
    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, bounds(0.0, 0.0, 2.0, 2.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * with a custom ConvergenceChecker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-4;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, customChecker);
        PointValuePair result = optimizer.optimize(function, bounds(0.0, 0.0, 2.0, 2.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}.
     */
    @Test
    public void testCustomLineSearchTolerances() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, 1e-5, 1e-5);
        PointValuePair result = optimizer.optimize(function, bounds(0.0, 0.0, 2.0, 2.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     * with a custom ConvergenceChecker.
     */
    @Test
    public void testCustomLineSearchTolerancesAndConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-4;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, 1e-5, 1e-5, customChecker);
        PointValuePair result = optimizer.optimize(function, bounds(0.0, 0.0, 2.0, 2.0), GoalType.MINIMIZE);
        Assert.assertEquals(0.0, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.0, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction, PointValuePair[], GoalType)}
     * with invalid argument bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentBounds() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, null, GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction, PointValuePair[], GoalType)}
     * with a function that is not multivariate.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonMultivariateFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, bounds(0.0, 0.0, 0.0, 0.0), GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction, PointValuePair[], GoalType)}
     * with a function that throws an exception.
     */
    @Test(expected = RuntimeException.class)
    public void testFunctionWithException() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) throws RuntimeException {
                if (x[0] > 0) {
                    throw new RuntimeException("Function exception.");
                }
                return 0;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, bounds(-1.0, 1.0, -1.0, 1.0), GoalType.MINIMIZE);
    }

    /**
     * Test method for {@link PowellOptimizer#optimize(MultivariateFunction, PointValuePair[], GoalType)}
     * with a function that returns infinity.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionWithInfinity() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, bounds(-1.0, 1.0, -1.0, 1.0), GoalType.MINIMIZE);
    }
}

