
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test when doOptimize() is not supported
        try {
            optimizer.doOptimize();
        } catch (UnsupportedOperationException e) {
            assertEquals("doOptimize() is not supported", e.getMessage());
        }
    }

    @Test
    public void testOptimize1() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        assertEquals(0, result, 0);
    }

    @Test
    public void testOptimize2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
        assertEquals(0, result, 0);
    }

    @Test
    public void testLocalMin1() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double result = optimizer.localMin(true, f, GoalType.MINIMIZE, -10, 0, 10, 0.01, 0.01);
        assertEquals(0, result, 0);
    }

    @Test
    public void testLocalMin2() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        double result = optimizer.localMin(false, f, GoalType.MINIMIZE, -10, 0, 10, 0.01, 0.01);
        assertEquals(0, result, 0);
    }
}
