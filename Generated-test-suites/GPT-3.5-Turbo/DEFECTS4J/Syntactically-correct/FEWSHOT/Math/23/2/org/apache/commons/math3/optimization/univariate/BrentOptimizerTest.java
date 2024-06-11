package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        double rel = 1.0;
        double abs = 0.1;

        // No convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        double min = 0.0;
        double max = 1.0;
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(new MyFunction());
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new Interval(min, max));
        assertEquals(0.5, result.getPoint(), 0.0001);
        assertEquals(0.25, result.getValue(), 0.0001);

        // With convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
        optimizer = new BrentOptimizer(rel, abs, checker);
        result = optimizer.optimize(function, GoalType.MINIMIZE, new Interval(min, max));
        assertEquals(0.5, result.getPoint(), 0.0001);
        assertEquals(0.25, result.getValue(), 0.0001);
    }

    private class MyFunction implements UnivariateFunction {
        public double value(double x) {
            return (x - 0.5) * (x - 0.5);
        }
    }

    private class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return Math.abs(current.getValue() - previous.getValue()) < 1e-6;
        }
    }
}