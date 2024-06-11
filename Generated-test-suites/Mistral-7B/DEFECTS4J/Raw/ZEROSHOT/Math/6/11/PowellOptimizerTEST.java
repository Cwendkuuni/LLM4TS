package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Minimum relative tolerance.
     */
    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp(1d);

    /**
     * Tests the Powell optimizer with a simple function.
     */
    @Test
    public void testSimpleFunction() {
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

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f);

        Assert.assertEquals(0d, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0d, result.getPoint()[1], 1e-6);
        Assert.assertEquals(0d, result.getValue(), 1e-6);
    }

    /**
     * Tests the Powell optimizer with a function having multiple local minima.
     */
    @Test
    public void testComplexFunction() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double value = 0;
                for (int i = 0; i < x.length; i++) {
                    value += Math.sin(x[i]) * Math.sin(x[i]) * x[i];
                }
                return value;
            }

            @Override
            public int getNumberOfVariables() {
                return 10;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(f);

        // A global minimum is approximately [-3.2155, 2.1751, -1.4801, 0.3184,
        // 1.0711, -0.5828, 0.8327, 1.2759, 0.0725, 0.035]
        double[] expected = new double[]{ -3.2155, 2.1751, -1.4801, 0.3184, 1.0711,
                -0.5828, 0.8327, 1.2759, 0.0725, 0.035 };
        Assert.assertArrayEquals(expected, result.getPoint(), 1e-4);
        Assert.assertEquals(0d, result.getValue(), 1e-4);
    }

    /**
     * Tests the Powell optimizer with a user-defined convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-Math.pow(x[0], 2.0)) - 0.5 * Math.pow(x[1], 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        final double relTol = 1e-4;
        final double absTol = 1e-4;

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous,
                                    PointValuePair current) {
                double[] p1 = previous.getPoint();
                double[] p2 = current.getPoint();
                double[] grad1 = PowellOptimizer.this.gradient(p1, f);
                double[] grad2 = PowellOptimizer.this.gradient(p2, f);
                List<Double> differences = new ArrayList<>();

                for (int i = 0; i < p1.length; i++) {
                    differences.add(Math.abs(grad1[i] - grad2[i]));
                }

                boolean stop = false;
                for (Double difference : differences) {
                    if (difference > relTol) {
                        stop = true;
                        break;
                    }
                }

                if (stop) {
                    return true;
                }

                double maxValue = 0;
                for (Double difference : differences) {
                    maxValue = Math.max(maxValue, difference);
                }

                return maxValue < absTol;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(relTol, absTol, customChecker);
        PointValuePair result = optimizer.optimize(f);

        Assert.assertEquals(0d, result.getValue(), 1e-4);
    }

    /**
     * Tests the Powell optimizer with a user-defined convergence checker and
     * custom line search tolerances.
     */
    @Test
    public void testCustomLineSearchTolerances() {
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.exp(-Math.pow(x[0], 2.0)) - 0.5 * Math.pow(x[1], 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        final double relTol = 1e-4;
        final double absTol = 1e-4;
        final double lineRelTol = 1e-5;
        final double lineAbsTol = 1e-5;

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous,
                                    PointValuePair current) {
                double[] p1 = previous.getPoint();
                double[] p2 = current.getPoint();
                double[] grad1 = PowellOptimizer.this.gradient(p1, f);
                double[] grad2 = PowellOptimizer.this.gradient(p2, f);
                List<Double> differences = new ArrayList<>();

                for (int i = 0; i < p1.length; i++) {
                    differences.add(Math.abs(grad1[i] - grad2[i]));
                }

                boolean stop = false;
                for (Double difference : differences) {
                    if (difference > relTol) {
                        stop = true;
                        break;
                    }
                }

                if (stop) {
                    return true;
                }

                double maxValue = 0;
                for (Double difference : differences) {
                    maxValue = Math.max(maxValue, difference);
                }

                return maxValue < absTol;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(relTol, absTol, lineRelTol, lineAbsTol, customChecker);
        PointValuePair result = optimizer.optimize(f);

        Assert.assertEquals(0d, result.getValue(), 1e-4);
    }
}