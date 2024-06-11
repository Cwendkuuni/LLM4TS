package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.exp(-Math.abs(x)) - 0.5;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        assertEquals(-0.7126775993656124, result.getPoint(), 0.0001);
        assertEquals(-1.0752257561563281, result.getValue(), 0.0001);
    }

    @Test
    public void testBrentOptimizer_convergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.001;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, convergenceChecker);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.exp(-Math.abs(x)) - 0.5;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        assertEquals(-0.7126775993656124, result.getPoint(), 0.0001);
        assertEquals(-1.0752257561563281, result.getValue(), 0.0001);
        assertTrue(convergenceChecker.converged(1, null, result));
    }

    @Test
    public void testBrentOptimizer_noConvergence() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + Math.exp(-Math.abs(x)) - 0.5;
            }
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0, GoalType.MINIMIZE);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_invalidAbsThreshold() {
        new BrentOptimizer(1.0, -0.001, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_invalidRelThreshold() {
        new BrentOptimizer(0.0, 0.001, null);
    }
}