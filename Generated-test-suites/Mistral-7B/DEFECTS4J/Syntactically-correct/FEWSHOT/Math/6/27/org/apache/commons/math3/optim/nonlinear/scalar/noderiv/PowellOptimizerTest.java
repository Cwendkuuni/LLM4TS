package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.multivariate.MultivariatePointValuePair;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerWithNegativeAbsoluteThreshold() {
        new PowellOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerWithRelativeThresholdTooSmall() {
        new PowellOptimizer(0.0, 1.0, null);
    }

    @Test
    public void testPowellOptimizerWithCustomChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeWithMinimizationGoalType() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[]{2.0, 3.0});
        MultivariatePointValuePair expected = new MultivariatePointValuePair(new double[]{0.0, 0.0}, 0.0);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 0.001);
        assertEquals(expected.getValue(), result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithMaximizationGoalType() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return -point[0] * point[0] - point[1] * point[1];
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[]{2.0, 3.0});
        MultivariatePointValuePair expected = new MultivariatePointValuePair(new double[]{0.0, 0.0}, 0.0);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 0.001);
        assertEquals(expected.getValue(), result.getValue(), 0.001);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
            assertTrue(iter > 10);
            return false;
        }
    }
}