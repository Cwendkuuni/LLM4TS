package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction function = (f, x) -> x[0] * x[0] + x[1] * x[1];
        double[] startPoint = new double[]{3.0, 3.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(1e-6, -1e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new PowellOptimizer(1e-9, 1e-6, null);
    }

    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new CustomConvergenceChecker());
        MultivariateFunction function = (f, x) -> Math.abs(x[0] - 2.0) + Math.abs(x[1] - 3.0);
        double[] startPoint = new double[]{1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{2.0, 3.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction function = (f, x) -> x[0] * x[0] + x[1] * x[1];
        double[] startPoint = new double[]{3.0, 3.0};
        double[] direction = new double[]{1.0, -1.0};
        UnivariatePointValuePair result = optimizer.lineSearch(startPoint, direction);
        assertEquals(-1.5, result.getPoint(), 1e-6);
        assertEquals(4.5 * 1.5 + 1.5 * 1.5, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(null, new double[]{0.0, 0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        optimizer.optimize(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, null);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            return Math.abs(previous.getValue() - current.getValue()) < 1e-6;
        }
    }
}