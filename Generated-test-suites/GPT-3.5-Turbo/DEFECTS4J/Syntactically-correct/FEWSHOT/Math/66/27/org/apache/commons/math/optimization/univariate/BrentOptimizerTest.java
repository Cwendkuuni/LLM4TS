package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test with a quadratic function f(x) = x^2 - 2x - 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2 * x - 1;
            }
        };
        
        try {
            double result = optimizer.optimize(f1, GoalType.MINIMIZE, -10, 10);
            assertEquals(1.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
        
        // Test with a linear function f(x) = 2x + 1
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };
        
        try {
            double result = optimizer.optimize(f2, GoalType.MAXIMIZE, -10, 10, 0);
            assertEquals(10.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
    }
    
    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test with a quadratic function f(x) = x^2 - 2x - 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 2 * x - 1;
            }
        };
        
        try {
            double result = optimizer.localMin(true, f1, GoalType.MINIMIZE, -10, 0, 10, 1e-14, 1e-10);
            assertEquals(1.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
        
        // Test with a linear function f(x) = 2x + 1
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return 2 * x + 1;
            }
        };
        
        try {
            double result = optimizer.localMin(false, f2, GoalType.MAXIMIZE, -10, 0, 10, 1e-14, 1e-10);
            assertEquals(10.0, result, 1e-10);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException");
        }
    }
}