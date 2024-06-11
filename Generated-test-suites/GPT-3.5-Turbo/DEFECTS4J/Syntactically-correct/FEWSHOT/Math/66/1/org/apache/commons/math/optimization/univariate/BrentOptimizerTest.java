package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {
    
    private final double EPSILON = 1e-10;

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();

        try {
            optimizer.doOptimize();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Pass
        }
    }

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Define the function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 2) * (x - 2);
            }
        };

        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4);
            assertEquals(2, result, EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Define the function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 2) * (x - 2);
            }
        };

        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 4, 3);
            assertEquals(2, result, EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Define the function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 2) * (x - 2);
            }
        };

        try {
            double result = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 2, 4, 0.0001, 0);
            assertEquals(2, result, EPSILON);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException: " + e.getMessage());
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException: " + e.getMessage());
        }
    }
}