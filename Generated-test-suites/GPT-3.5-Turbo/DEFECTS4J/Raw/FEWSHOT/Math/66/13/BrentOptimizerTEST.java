package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private static final double EPSILON = 1e-10;
    
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test when the function is defined on the interval (lo, hi)
        try {
            UnivariateRealFunction f = new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x;
                }
            };
            double optimum = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            assertEquals(0.0, optimum, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
        
        // Test when the function is not defined on the interval (lo, hi)
        try {
            UnivariateRealFunction f = new UnivariateRealFunction() {
                public double value(double x) {
                    return 1 / x;
                }
            };
            double optimum = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
            assertEquals(0.0, optimum, EPSILON);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, -1, 0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinNotStrictlyPositiveException2() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        try {
            optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 0.01, -0.001);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }
}