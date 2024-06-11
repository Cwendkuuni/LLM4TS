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
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-3, new SimpleUnivariateValueChecker(1e-3, 1e-3));
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-3);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getPoint(), 1e-3);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerWithNegativeAbsTol() {
        new PowellOptimizer(1.0, -1.0, null);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}.
     */
    @Test
    public void testPowellOptimizerWithCustomLineSearchTol() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return FastMath.pow(x[0], 2.0) + FastMath.pow(x[1], 2.0);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-3, 1e-4, 1e-3, 1e-4, new SimpleUnivariateValueChecker(1e-3, 1e-4));
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0, 1.0});
        assertEquals(0.0, result.getValue(), 1e-4);
        assertArrayEquals(new double[] {0.0, 0.0}, result.getPoint(), 1e-4);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerWithRelTolTooSmall() {
        new PowellOptimizer(0.5 * FastMath.ulp(1.0), 1e-3, new SimpleUnivariateValueChecker(1e-3, 1e-3));
    }
}