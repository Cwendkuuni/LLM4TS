package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstruction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructionWithNegativeAbsoluteThreshold() {
        new PowellOptimizer(0.001, -1.0e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructionWithTooSmallRelativeThreshold() {
        new PowellOptimizer(0.0, 1.0e-5);
    }

    @Test
    public void testCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5, checker);
        assertNotNull(optimizer);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5);
        MultivariateFunction function = mock(MultivariateFunction.class);

        double[] startPoint = {1.0, 2.0};
        PointValuePair expectedResult = new PointValuePair(new double[]{0.1, 0.2}, 0.01);

        when(function.value(startPoint)).thenReturn(1.5);

        optimizer.setFunction(function);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair result = optimizer.optimize();

        assertEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5);
        optimizer.setFunction(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5);
        optimizer.setStartPoint(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 1.0e-5);
        optimizer.setGoalType(null);
    }
}