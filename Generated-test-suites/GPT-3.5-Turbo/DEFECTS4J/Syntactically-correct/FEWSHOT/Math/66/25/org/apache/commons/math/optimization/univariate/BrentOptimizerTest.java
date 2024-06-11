package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {
    @Test
    public void testOptimize1() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                // function to be optimized
                return x * x - 4;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 1, 3, 2);
        assertEquals(2, result, 1e-6);
    }

    @Test
    public void testOptimize2() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                // function to be optimized
                return Math.sin(x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 2);
        assertEquals(3.141592653589793, result, 1e-6);
    }

    @Test
    public void testOptimize3() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                // function to be optimized
                return Math.exp(x) - x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, 2, 1);
        assertEquals(0, result, 1e-6);
    }
}