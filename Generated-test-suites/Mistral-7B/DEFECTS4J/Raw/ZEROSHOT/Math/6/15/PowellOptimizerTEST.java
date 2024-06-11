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
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for PowellOptimizer class.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.exp(-0.1 * FastMath.pow(x[0], 2)) +
                       FastMath.exp(-0.1 * FastMath.pow(x[1], 2)) +
                       FastMath.sin(x[0]) * FastMath.sin(x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8);
        PointValuePair solution = optimizer.optimize(function);

        double[] expectedSolution = new double[]{1.2071067811865475, 0.8012582360461297};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-8);
        assertEquals(2.160490465541684E-9, solution.getValue(), 1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new PowellOptimizer(0.5 * FastMath.ulp(1d), 1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1e-6, -1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThresholdAndCheckCustom() {
        ConvergenceChecker<PointValuePair> checker = (iter, previous, current) -> true;
        new PowellOptimizer(0.5 * FastMath.ulp(1d), 1e-8, checker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        new PowellOptimizer(1e-6, 1e-8, null);
    }

    @Test
    public void testCustomConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(FastMath.sin(x[0]) + Math.sin(x[1]), 2) + 1;
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> checker = (iter, previous, current) -> {
            double[] previousPoint = previous.getPoint();
            double[] currentPoint = current.getPoint();
            double[] p1 = MathArrays.copyOf(previousPoint);
            double[] p2 = MathArrays.copyOf(currentPoint);
            double[] direction = new double[2];

            for (int i = 0; i < 2; i++) {
                direction[i] = currentPoint[i] - previousPoint[i];
            }

            double fPrevious = function.value(previousPoint);
            double fCurrent = function.value(currentPoint);

            if (Math.abs(fCurrent - fPrevious) < 0.01) {
                return true;
            }

            double[] diff = MathArrays.copyOf(currentPoint);
            double fDiff = function.value(diff);
            if (fDiff < fCurrent) {
                return true;
            }

            return false;
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-8, checker);
        PointValuePair solution = optimizer.optimize(function);

        double[] expectedSolution = new double[]{0.7853981633974483, 0.7853981633974483};
        assertArrayEquals(expectedSolution, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }
}