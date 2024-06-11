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
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * Minimum relative tolerance.
     */
    private static final double MIN_RELATIVE_TOLERANCE = 2 * FastMath.ulp(1d);

    /**
     * Test for Powell optimizer with default settings.
     */
    @Test
    public void testDefaultOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += x[i] * x[i] * x[i];
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair result = optimizer.optimize(function);
        double[] expected = {0.5, 0.5, 0.5};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
    }

    /**
     * Test for Powell optimizer with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1, 2);
                }
                return sum;
            }
        };

        ConvergenceChecker<PointValuePair> convergenceChecker = (iter, previous, current) -> {
            double[] previousPoint = previous.getPoint();
            double[] currentPoint = current.getPoint();
            double[] expected = {1, 1, 1};
            assertArrayEquals(expected, previousPoint, 0.0001);
            assertArrayEquals(expected, currentPoint, 0.0001);
            return true;
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3, convergenceChecker);
        PointValuePair result = optimizer.optimize(function);
        double[] expected = {1, 1, 1};
        assertArrayEquals(expected, result.getPoint(), 0.0001);
    }

    /**
     * Test for Powell optimizer with different number of dimensions.
     */
    @Test
    public void testDifferentNumberOfDimensions() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 1, 2);
                }
                return sum;
            }
        };

        PowellOptimizer optimizer1 = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair result1 = optimizer1.optimize(function);
        double[] expected1 = {1};
        assertArrayEquals(expected1, result1.getPoint(), 0.0001);

        PowellOptimizer optimizer2 = new PowellOptimizer(1e-3, 1e-3);
        PointValuePair result2 = optimizer2.optimize(function, new double[]{1, 1});
        double[] expected2 = {1, 1};
        assertArrayEquals(expected2, result2.getPoint(), 0.0001);
    }

    /**
     * Test for Powell optimizer with a function that has no solution.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionWithNoSolution() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], -2);
                }
                return sum;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3);
        optimizer.optimize(function);
    }
}