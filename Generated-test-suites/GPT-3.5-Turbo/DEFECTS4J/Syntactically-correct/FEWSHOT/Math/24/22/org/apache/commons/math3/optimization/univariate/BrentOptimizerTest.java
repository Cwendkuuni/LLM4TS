package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BrentOptimizerTest {
    private final double relativeThreshold = 0.01;
    private final double absoluteThreshold = 0.001;

    private class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return (Math.abs(current.getValue() - previous.getValue()) < 0.001);
        }
    }

    @Test
    public void testDoOptimizeMinimize() {
        double min = -10;
        double max = 10;
        UnaryFunction objective = new UnaryFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 3, 2); // (x - 3)^2
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, new DummyConvergenceChecker());
        optimizer.optimize(objective, GoalType.MINIMIZE, min, max);
        UnivariatePointValuePair result = optimizer.getResult();
        double expected = 3;
        assertTrue(Precision.equals(result.getPoint(), expected, 0.001));
    }

    @Test
    public void testDoOptimizeMaximize() {
        double min = -10;
        double max = 10;
        UnaryFunction objective = new UnaryFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 3, 2); // -(x - 3)^2
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, new DummyConvergenceChecker());
        optimizer.optimize(objective, GoalType.MAXIMIZE, min, max);
        UnivariatePointValuePair result = optimizer.getResult();
        double expected = 3;
        assertTrue(Precision.equals(result.getPoint(), expected, 0.001));
    }
}