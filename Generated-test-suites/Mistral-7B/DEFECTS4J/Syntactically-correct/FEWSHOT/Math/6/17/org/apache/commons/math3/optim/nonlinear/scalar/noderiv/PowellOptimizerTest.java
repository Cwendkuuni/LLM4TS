package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.maximization.Maximizer;
import org.apache.commons.math3.optim.minimization.Minimizer;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        assertNull(optimizer.getStartPoint());
    }

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.0}, optimizer.getStartPoint());
    }

    @Test
    public void testConstructorsWithRelAndAbs() {
        PowellOptimizer optimizer1 = new PowellOptimizer(0.001, 0.01);
        PowellOptimizer optimizer2 = new PowellOptimizer(0.001, 0.01, 1e-5, 1e-3);
        PowellOptimizer optimizer3 = new PowellOptimizer(0.001, 0.01, 1e-6, 1e-4);

        assertEquals(GoalType.MINIMIZE, optimizer1.getGoalType());
        assertEquals(GoalType.MINIMIZE, optimizer2.getGoalType());
        assertEquals(GoalType.MINIMIZE, optimizer3.getGoalType());

        assertArrayEquals(new double[]{0.0}, optimizer1.getStartPoint());
        assertArrayEquals(new double[]{0.0}, optimizer2.getStartPoint());
        assertArrayEquals(new double[]{0.0}, optimizer3.getStartPoint());

        assertEquals(0.001, optimizer1.getRelativeThreshold(), 0.0001);
        assertEquals(0.01, optimizer1.getAbsoluteThreshold(), 0.001);
        assertEquals(1e-5, optimizer2.getRelativeThreshold(), 0.0001);
        assertEquals(1e-3, optimizer2.getAbsoluteThreshold(), 0.001);
        assertEquals(1e-6, optimizer3.getRelativeThreshold(), 0.0001);
        assertEquals(1e-4, optimizer3.getAbsoluteThreshold(), 0.001);
    }

    @Test
    public void testCustomCheckerConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, new CustomConvergenceChecker());
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(new double[]{0.0}, optimizer.getStartPoint());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        UnivariateFunction function = x -> Math.pow(x, 2);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);

        assertEquals(-1, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeMinimization() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        UnivariateFunction function = x -> Math.pow(x, 2);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MINIMIZE);

        assertEquals(-1, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimizeMaximization() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        UnivariateFunction function = x -> -Math.pow(x, 2);

        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MAXIMIZE);

        assertEquals(1, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(0.001, -0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroRelativeThreshold() {
        new PowellOptimizer(0.0, 0.01);
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double previousValue = previous.getValue();
            double currentValue = current.getValue();
            return (previousValue > currentValue && currentValue < 0) ||
                   (previousValue < currentValue && currentValue > 0);
        }
    }
}