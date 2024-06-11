package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };

        double min = 2.0;
        double startValue = 3.0;

        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, min, min + 1.0, startValue);
            assertEquals(min, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2);
            }
        };

        double min = 2.0;
        double startValue = 3.0;

        try {
            double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, min + 1.0, startValue);
            assertEquals(min, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGoalTypeMinimizeWithInvalidArgument() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double min = 2.0;
        double startValue = 3.0;

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, null, min, min + 1.0, startValue));
    }

    @Test
    public void testGoalTypeMaximizeWithInvalidArgument() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double min = 2.0;
        double startValue = 3.0;

        assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, GoalType.MAXIMIZE, min, min + 1.0, startValue));
    }

    @Test
    public void testMinimizationWithInvalidStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };

        double min = 2.0;
        double startValue = 1.5;

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, min + 1.0, startValue);
            fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testMaximizationWithInvalidStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.pow(x - 2, 2);
            }
        };

        double min = 2.0;
        double startValue = 1.5;

        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, min, min + 1.0, startValue);
            fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testMinimizationNonexistentMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double min = Double.POSITIVE_INFINITY;
        double startValue = -5.0;

        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, min, min + 1.0, startValue);
            fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testMaximizationNonexistentMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -Math.sin(x) / x;
            }
        };

        double min = Double.NEGATIVE_INFINITY;
        double startValue = 5.0;

        try {
            double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, min + 1.0, startValue);
            fail("Expected MaxIterationsExceededException or FunctionEvaluationException");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Expected
        }
    }
}