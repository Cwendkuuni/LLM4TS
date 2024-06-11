package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(-1, 0.0));
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(0.0, -1.0));
    }

    @Test
    public void testBrentOptimizerConstructorWithRelAndAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(1e-8, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerConstructorWithRelOnly() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 0.0);
        assertNull(optimizer.getAbsoluteThreshold());
    }

    @Test
    public void testBrentOptimizerConstructorWithAbsOnly() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1e-8);
        assertNull(optimizer.getRelativeThreshold());
        assertEquals(1e-8, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerDoOptimize() {
        // Test with null function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6);
        assertThrows(IllegalArgumentException.class, () -> optimizer.doOptimize());

        // Test with a simple function
        FunctionTestSimple function = new FunctionTestSimple();
        optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(FunctionTestSimple.X_MIN, result.getX(), 0.0);
        assertEquals(FunctionTestSimple.MIN_VALUE, result.getValue(), 0.0);

        // Test with a custom convergence checker
        ConvergenceChecker<UnivariatePointValuePair> customChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                double currentValue = current.getValue();
                double previousValue = previous.getValue();
                return (iteration > 100) || (Math.abs(currentValue - previousValue) < 1e-12);
            }
        };
        optimizer = new BrentOptimizer(1e-6, 1e-8, customChecker);
        result = optimizer.doOptimize();
        assertEquals(FunctionTestSimple.X_MIN, result.getX(), 0.0);
        assertEquals(FunctionTestSimple.MIN_VALUE, result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerGoalType() {
        FunctionTestSimple function = new FunctionTestSimple();
        BrentOptimizer optimizerGoalMin = new BrentOptimizer(1e-6, 1e-8, null);
        optimizerGoalMin.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair resultMin = optimizerGoalMin.doOptimize();
        assertEquals(FunctionTestSimple.X_MIN, resultMin.getX(), 0.0);
        assertEquals(FunctionTestSimple.MIN_VALUE, resultMin.getValue(), 0.0);

        BrentOptimizer optimizerGoalMax = new BrentOptimizer(1e-6, 1e-8, null);
        optimizerGoalMax.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair resultMax = optimizerGoalMax.doOptimize();
        assertEquals(FunctionTestSimple.X_MAX, resultMax.getX(), 0.0);
        assertEquals(FunctionTestSimple.MAX_VALUE, resultMax.getValue(), 0.0);
    }
}

class FunctionTestSimple {
    static final double X_MIN = 1.0;
    static final double MIN_VALUE = 2.0;
    static final double X_MAX = 3.0;
    static final double MAX_VALUE = -1.0;

    double computeObjectiveValue(double x) {
        if (x < X_MIN) {
            return MIN_VALUE + (x - X_MIN) * (x - X_MIN);
        } else if (x > X_MAX) {
            return MAX_VALUE + (x - X_MAX) * (x - X_MAX);
        } else {
            return 0.0;
        }
    }
}