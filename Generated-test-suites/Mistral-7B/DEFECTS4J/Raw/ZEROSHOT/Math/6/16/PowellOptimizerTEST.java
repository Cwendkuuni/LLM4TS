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
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}.
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooSmallException
     */
    @Test
    public void testPowellOptimizerWithCustomChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 2, 2);
                }
                return sum;
            }

            @Override
            public int getNumberOfVariables() {
                return x.length;
            }
        };

        ConvergenceChecker<PointValuePair> customChecker = (iter, p1, p2) -> {
            double[] p1Values = p1.getPoint();
            double[] p2Values = p2.getPoint();
            double p1Value = p1.getValue();
            double p2Value = p2.getValue();

            double maxError = FastMath.max(FastMath.abs(p1Value - p2Value), 1);
            int numIterations = iter;

            // Simulate the convergence of the optimizer
            // after a certain number of iterations
            if (numIterations > 100) {
                return true;
            }

            Assert.assertTrue("Error is too large. Iteration: " + numIterations, maxError < 0.01);

            return false;
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-6, customChecker);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.5, 2.5, 2.5});

        Assert.assertEquals("Incorrect minimum found", 0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals("Incorrect minimum coordinates", new double[]{2.0, 2.0, 2.0}, result.getPoint());
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}.
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooSmallException
     */
    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 2, 2);
                }
                return sum;
            }

            @Override
            public int getNumberOfVariables() {
                return x.length;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.5, 2.5, 2.5});

        Assert.assertEquals("Incorrect minimum found", 0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals("Incorrect minimum coordinates", new double[]{2.0, 2.0, 2.0}, result.getPoint());
    }
}