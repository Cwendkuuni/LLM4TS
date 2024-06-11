package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.goaltypes.GoalType;

import java.util.Arrays;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1.0E-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0E-5, -1.0E-5, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new BrentOptimizer(1.0E-8, 1.0E-5, null);
    }

    @Test
    public void testBrentOptimizerWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                    UnivariatePointValuePair current) {
                return current.getValue() < 1.0E-5;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, checker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(1.0E-5, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(0.0, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize_InitialBoundsNotOrdered() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        double[] testFunction = { 1.0, 2.0, 3.0, 4.0, 5.0 };

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 4.0, 1.0);
        assertNull(result);
    }

    @Test
    public void testDoOptimize_InitialBoundsOrderedMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        double[] testFunction = { 1.0, 2.0, 3.0, 4.0, 5.0 };

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 4.0);
        assertEquals(4.0, result.getX(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_InitialBoundsOrderedMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        double[] testFunction = { 1.0, 2.0, 3.0, 4.0, 5.0 };

        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 4.0);
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(5.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_NoSolutionFound() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        double[] testFunction = { 1.0, 2.0, 3.0, 4.0, 5.0 };

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 0.0, 0.0);
        assertNull(result);
    }

    @Test
    public void testDoOptimize_SolutionFound() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        double[] testFunction = { 1.0, 2.0, 3.0, 4.0, 5.0 };

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 4.0);
        assertEquals(4.0, result.getX(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithChecker() {
        double[] testFunction = { -1.0, 1.0, 3.0, 5.0, 7.0 };
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                    UnivariatePointValuePair current) {
                return current.getValue() < 0.1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 0.0, 10.0);
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(-7.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithNonMonotonicFunction() {
        double[] testFunction = { 1.0, 3.0, 2.0, 5.0, 4.0 };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 5.0);
        assertEquals(3.0, result.getX(), 0.0);
        assertEquals(2.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithIterations() {
        double[] testFunction = { 1.0, 3.0, 2.0, 5.0, 4.0 };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 5.0);
        assertEquals(3.0, result.getX(), 0.0);
        assertEquals(2.0, result.getValue(), 0.0);

        int iterations = optimizer.getIterations();
        assertEquals(10, iterations);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithIterationsAndChecker() {
        double[] testFunction = { -1.0, 1.0, 3.0, 5.0, 7.0 };
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                    UnivariatePointValuePair current) {
                return current.getValue() < 0.1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 0.0, 10.0);
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(-7.0, result.getValue(), 0.0);

        int iterations = optimizer.getIterations();
        assertEquals(15, iterations);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithInitialX() {
        double[] testFunction = { 1.0, 3.0, 2.0, 5.0, 4.0 };

        BrentOptimizer optimizer = new BrentOptimizer(1.0E-5, 0.0, null);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setInitialX(3.0);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 1.0, 5.0);
        assertEquals(3.0, result.getX(), 0.0);
        assertEquals(2.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_SolutionFoundWithInitialXAndChecker() {
        double[] testFunction = { -1.0, 1.0, 3.0, 5.0, 7.0 };
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous,
                    UnivariatePointValuePair current) {
                return current.getValue() < 0.1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setInitialX(1.0);

        UnivariatePointValuePair result = optimizer.doOptimize(testFunction, 0.0, 10.0);
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(-7.0, result.getValue(), 0.0);
    }
}