package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization. multivariate.MultivariatePointValuePair;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> true;
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, customChecker);
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.01, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(customChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeSimpleCase() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 1.0, 2.0);

        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testUserDefinedConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> {
            double[] prevPoint = previous.getPoint();
            double[] currentPoint = current.getPoint();
            double[] startPoint = {1.0, 1.0};

            double prevDistance = Math.sqrt(Math.pow(prevPoint[0] - startPoint[0], 2.0) + Math.pow(prevPoint[1] - startPoint[1], 2.0));
            double currentDistance = Math.sqrt(Math.pow(currentPoint[0] - startPoint[0], 2.0) + Math.pow(currentPoint[1] - startPoint[1], 2.0));

            return prevDistance < currentDistance;
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01, customChecker);
        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 1.0, 2.0);

        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);

        // Test convergence checker
        double[] startingValues = {1.1, 1.1};
        PointValuePair previousValues = new PointValuePair(startingValues, 2.0);
        PointValuePair currentValues = new PointValuePair(startingValues, 1.99);

        // The optimizer will not converge here because the checker always
        // returns true
        assertFalse(optimizer.getConvergenceChecker().converged(0, previousValues, currentValues));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeThreshold() {
        new PowellOptimizer(-0.001, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new PowellOptimizer(0.001, -0.01);
    }

    @Test
    public void testLineSearchOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);

        double[] startPoint = {1.0, 1.0};
        MultivariateFunction function = f -> Math.pow(f[0] - 1.0, 2.0) + Math.pow(f[1] - 1.0, 2.0);

        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);

        double[] point = result.getPoint();
        double[] direction = new double[]{1.0, -1.0};

        UnivariatePointValuePair lineSearchResult = optimizer.getLineSearch().search(point, direction);
        assertEquals(0.0, lineSearchResult.getValue(), 0.001);
        assertEquals(1.0, lineSearchResult.getPoint(), 0.001);
    }
}