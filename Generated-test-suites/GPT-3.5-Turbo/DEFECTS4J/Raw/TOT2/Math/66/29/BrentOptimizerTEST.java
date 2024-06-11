package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5, 2.5);
            assertEquals(2.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
            assertEquals(2.0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLocalMinException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        };
        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, -5, 5);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

    @Test
    public void testExceptionHandling() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return 0;
            }
        };
        try {
            optimizer.optimize(function, GoalType.MAXIMIZE, -5, 5, 0);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}