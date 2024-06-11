package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math.exception.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            assertEquals("java.lang.UnsupportedOperationException", ex.toString());
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 2);
            assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            assertEquals("", ex.toString());
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            assertEquals("", ex.toString());
        }
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1e-10, 1e-10);
            assertEquals(0.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException ex) {
            assertEquals("", ex.toString());
        }
    }
}