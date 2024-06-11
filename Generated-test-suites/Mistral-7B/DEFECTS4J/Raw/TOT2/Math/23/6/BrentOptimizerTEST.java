package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x + 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, 1.e-6);
        UnivariateOptimizerResult result = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleValueChecker(1.e-9));
        assertEquals(result.getPoint().getX(), -1.0, 1e-6);
        assertEquals(result.getValue(), 2.0, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(-1.0, 1.e-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0.5, 1.e-6, null);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x + 1;
            }
        };

        // Change the goal type to MAXIMIZE
        BrentOptimizer optimizer = new BrentOptimizer(1.e-6, 1.e-6, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariateOptimizerResult result = optimizer.optimize(f, null);
        assertEquals(result.getPoint().getX(), 1.0, 1e-6);
        assertEquals(result.getValue(), 2.0, 1e-6);
    }

    @Test
    public void testBrentOptimizerNoInitialGuess() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0, null);
        UnivariateOptimizerResult result = optimizer.optimize(f, null);
        assertEquals(result.getPoint().getX(), 0.0, 1e-6);
        assertEquals(result.getValue(), 0.0, 1e-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        Function f = new Function() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.cos(x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.e-10, 1.e-12);
        ConvergenceChecker<UnivariatePointValuePair> checker
            = new CustomConvergenceChecker(1.e-6);
        UnivariateOptimizerResult result = optimizer.optimize(f, null, checker);
        assertEquals(result.getPoint().getX(), Math.PI, 1e-6);
        assertEquals(result.getValue(), -1.0, 1e-6);
    }
}

class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

    private final double tolerance;

    public CustomConvergenceChecker(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        double difference = Math.abs(current.getValue() - previous.getValue());
        return difference < tolerance;
    }
}