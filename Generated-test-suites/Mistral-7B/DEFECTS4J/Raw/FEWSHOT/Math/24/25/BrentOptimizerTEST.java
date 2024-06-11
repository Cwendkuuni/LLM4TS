package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizerConstruction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertTrue(optimizer instanceof BrentOptimizer);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.00001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.00001);

        BrentOptimizer optimizer2 = new BrentOptimizer(0.01);
        assertTrue(optimizer2 instanceof BrentOptimizer);
        assertEquals(0.01, optimizer2.getRelativeThreshold(), 0.00001);
        assertEquals(0.0, optimizer2.getAbsoluteThreshold(), 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        new BrentOptimizer(0.005, 0.0, null);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithMinFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        double functionMin = 1.5;
        double[] functionValues = {2.1, 1.6, functionMin, 1.9, 1.8};
        UnivariatePointValuePair expectedResult = new UnivariatePointValuePair(functionMin, functionMin);

        UnivariatePointValuePair result = optimizer.doOptimize(functionValues, 0, functionValues.length, GoalType.MINIMIZE);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testBrentOptimizerDoOptimizeWithMaxFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        double functionMax = 5.2;
        double[] functionValues = {1.8, 2.3, 3.7, functionMax, 4.1};
        UnivariatePointValuePair expectedResult = new UnivariatePointValuePair(functionMax, -functionMax);

        UnivariatePointValuePair result = optimizer.doOptimize(functionValues, 0, functionValues.length, GoalType.MAXIMIZE);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new BrentOptimizer.ConvergenceChecker() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return previous.getValue() > 0 && current.getValue() > 0 && Math.abs(current.getValue() - previous.getValue()) < 0.0001;
            }
        });

        double[] functionValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        optimizer.optimize(functionValues, 0, functionValues.length);

        UnivariatePointValuePair result = optimizer.getBestSolution();
        assertNull(result);

        functionValues = new double[]{1.1, 1.2, 1.5, 1.7, 2.0};
        optimizer.optimize(functionValues, 0, functionValues.length);

        result = optimizer.getBestSolution();
        assertTrue(result != null);
        assertTrue(Math.abs(result.getValue() - 1.5) < 0.001);
        assertTrue(Math.abs(result.getX() - 1.5) < 0.001);
    }
}