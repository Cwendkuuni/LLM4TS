package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testOptimize_GoalType() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Example function, can be any function
            }
        };

        assertEquals(0.0, optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0), 1e-10);
        assertEquals(9.0, optimizer.optimize(f, GoalType.MAXIMIZE, -3.0, 5.0), 1e-10);
    }

    @Test
    public void testOptimize_GoalType_Range() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Example function, can be any function
            }
        };

        assertEquals(0.0, optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, -0.5), 1e-10);
        assertEquals(9.0, optimizer.optimize(f, GoalType.MAXIMIZE, -3.0, 5.0, 4.0), 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_GoalType_InvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Example function, can be any function
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, -0.5, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_GoalType_InvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Example function, can be any function
            }
        };

        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, -0.5, 1.0, -0.5);
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Example function, can be any function
            }
        };

        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, -1.0, -0.5, 1.0, 1e-10, 1e-10);
        assertEquals(-1.0, result, 1e-10);
    }
}