package org.apache.commons.cli;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        try {
            new BrentOptimizer(-0.1, 0.1, null);
            assert false;
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, 0.1, null);
            assert false;
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        new BrentOptimizer(1.0, 0.1, null);
    }

    @Test
    public void testBrentOptimizerConstructionWithChecker() {
        new BrentOptimizer(1.0, 0.1, (convergenceChecker) -> true);
    }

    @Test
    public void testBrentOptimizerDefault() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertEquals(1.0, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.1, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker
            = (iter, previous, current) -> false;
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        assertEquals(1.0, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.1, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2));
        assertEquals(-1.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2));
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker
            = (iter, previous, current) -> iter > 5;
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.pow(x, 2));
        assertEquals(-1.0, result.getX(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testOptimizeWithCustomFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x) * Math.cos(x));
        assertEquals(0.0, result.getX(), 0.01);
        assertEquals(-1.1127363758141647E-17, result.getValue(), 1.1127363758141647E-18);
    }

    @Test
    public void testOptimizeWithCustomFunctionAndCustomGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x) * Math.cos(x));
        assertEquals(Math.PI, result.getX(), 0.01);
        assertEquals(1.2247448713915959E-16, result.getValue(), 1.2247448713915959E-16);
    }

    @Test
    public void testOptimizeWithCustomFunctionAndConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker
            = (iter, previous, current) -> iter > 5;
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1, convergenceChecker);
        UnivariatePointValuePair result = optimizer.optimize(x -> Math.sin(x) * Math.cos(x));
        assertEquals(0.0, result.getX(), 0.01);
        assertEquals(-1.1127363758141647E-17, result.getValue(), 1.1127363758141647E-18);
    }
}