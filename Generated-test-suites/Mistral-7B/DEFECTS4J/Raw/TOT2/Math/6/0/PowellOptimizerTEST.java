package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    private PowellOptimizer optimizer;
    private double[] startPoint = {1.0, 2.0};
    private UnivariateFunction testFunction;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(1.0e-4, 1.0e-4);
        testFunction = (UnivariateFunction) (x -> x[0] * x[0] + x[1] * x[1]);
    }

    @Test
    public void testMinimization() {
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, startPoint);
        assertEquals(2.0, result.getPoint()[0], 1e-6);
        assertEquals(2.0, result.getPoint()[1], 1e-6);
        assertEquals(3.0, result.getValue(), 1e-6);
    }

    @Test
    public void testMaximization() {
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, startPoint);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(3.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(-1.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, -1.0);
    }

    @Test
    public void testMinimizationWithCustomFunction() {
        UnivariateFunction testFunction = (x -> x[0] * Math.log(x[1]));
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, new double[]{2.0, 1.0});
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGoalType() {
        optimizer.optimize(testFunction, null, startPoint);
    }

    @Test
    public void testMinimizationWithBracket() {
        double[] bracket = {1.0, 2.0, 3.0};
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, bracket);
        assertEquals(1.5, result.getPoint()[0], 1e-6);
        assertEquals(1.5, result.getPoint()[1], 1e-6);
        assertEquals(2.25, result.getValue(), 1e-6);
    }

    @Test
    public void testMaximizationWithBracket() {
        double[] bracket = {-1.0, -2.0, -3.0};
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, bracket);
        assertEquals(-1.5, result.getPoint()[0], 1e-6);
        assertEquals(-1.5, result.getPoint()[1], 1e-6);
        assertEquals(2.25, result.getValue(), 1e-6);
    }
}