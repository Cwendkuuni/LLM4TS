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
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0], 2) + FastMath.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);
        PointValuePair optimum = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1, -1});

        Assert.assertArrayEquals(new double[]{0, 0}, optimum.getPoint(), 1e-5);
        Assert.assertEquals(0.0, optimum.getValue(), 1e-5);

        optimizer = new PowellOptimizer(1e-5, 1e-5, new SimpleUnivariateValueChecker(1e-3, 1e-3));
        optimizer.setMaxIterations(100);
        optimum = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1, -1});

        Assert.assertArrayEquals(new double[]{0, 0}, optimum.getPoint(), 1e-3);
        Assert.assertEquals(0.0, optimum.getValue(), 1e-3);

        optimizer = new PowellOptimizer(1e-3, 1e-3, 1e-3, 1e-3, null);
        optimizer.setMaxIterations(100);
        optimum = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1, -1});

        Assert.assertArrayEquals(new double[]{0, 0}, optimum.getPoint(), 1e-3);
        Assert.assertEquals(0.0, optimum.getValue(), 1e-3);

        optimizer = new PowellOptimizer(1e-5, 1e-5, new BracketFinder() {
            @Override
            public double[] bracket(UnivariateFunction f, GoalType goal, double initialStep, double stepFactor) {
                double a = initialStep;
                double b = 3 * initialStep;
                double c;

                while (true) {
                    double fA = f.value(a);
                    double fB = f.value(b);

                    if (goal == GoalType.MINIMIZE) {
                        if (fA < fB) {
                            c = b;
                            b = a;
                            a = c;
                        }
                        if (fA < 0) {
                            return new double[]{a, b};
                        }
                    } else {
                        if (fA > fB) {
                            c = b;
                            b = a;
                            a = c;
                        }
                        if (fA > 0) {
                            return new double[]{a, b};
                        }
                    }

                    double fC = f.value(a + (b - a) / 2);
                    if (goal == GoalType.MINIMIZE) {
                        if (fA < fC) {
                            b = a + (b - a) / 2;
                        } else {
                            a = a + (b - a) / 2;
                        }
                    } else {
                        if (fA > fC) {
                            b = a + (b - a) / 2;
                        } else {
                            a = a + (b - a) / 2;
                        }
                    }
                }
            }
        });
        optimizer.setMaxIterations(100);
        optimum = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1, -1});

        Assert.assertArrayEquals(new double[]{0, 0}, optimum.getPoint(), 1e-3);
        Assert.assertEquals(0.0, optimum.getValue(), 1e-3);

        // Check that the powell optimizer with a non-positive absolute threshold
        // raises an exception
        try {
            new PowellOptimizer(1e-5, 0, null);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        // Check that the powell optimizer with a non-positive relative threshold
        // raises an exception
        try {
            new PowellOptimizer(0, 1e-5, null);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }

        // Check that with a non-null checker, the convergence condition is respected
        optimizer = new PowellOptimizer(1e-5, 1e-5, 1e-3, 1e-3, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                return Arrays.equals(previous.getPoint(), current.getPoint()) &&
                       Math.abs(previous.getValue() - current.getValue()) < 1e-3;
            }
        });
        optimizer.setMaxIterations(100);
        optimum = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{-1, -1});

        Assert.assertArrayEquals(new double[]{0, 0}, optimum.getPoint(), 1e-3);
        Assert.assertEquals(0.0, optimum.getValue(), 1e-3);
    }
}