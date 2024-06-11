
package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        // Set up the optimizer
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1E-9);
        optimizer.setMaximalIterationCount(100);

        double min = 0.0;
        double max = 10.0;

        // Define the target function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 5) * (x - 5);
            }
        };

        try {
            // Minimize the function
            double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);

            // Verify the result
            assertEquals(5.0, result, 1E-9);
        } catch (MaxIterationsExceededException e) {
            fail("Max iteration count exceeded.");
        } catch (FunctionEvaluationException e) {
            fail("Error evaluating function.");
        }

    }

    @Test
    public void testOptimizeMaximize() {
        // Set up the optimizer
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1E-9);
        optimizer.setMaximalIterationCount(100);

        double min = 0.0;
        double max = 10.0;

        // Define the target function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return -(x - 5) * (x - 5);
            }
        };

        try {
            // Maximize the function
            double result = optimizer.optimize(function, GoalType.MAXIMIZE, min, max);

            // Verify the result
            assertEquals(5.0, result, 1E-9);
        } catch (MaxIterationsExceededException e) {
            fail("Max iteration count exceeded.");
        } catch (FunctionEvaluationException e) {
            fail("Error evaluating function.");
        }

    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidEpsilon() {
        // Set up the optimizer
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0.0);
        optimizer.setMaximalIterationCount(100);

        double min = 0.0;
        double max = 10.0;

        // Define the target function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 5) * (x - 5);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        } catch (MaxIterationsExceededException e) {
            fail("Max iteration count exceeded.");
        } catch (FunctionEvaluationException e) {
            fail("Error evaluating function.");
        }

    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeInvalidTolerance() {
        // Set up the optimizer
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1E-9);
        optimizer.setMaximalIterationCount(0);

        double min = 0.0;
        double max = 10.0;

        // Define the target function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 5) * (x - 5);
            }
        };

        try {
            optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        } catch (MaxIterationsExceededException e) {
            fail("Max iteration count exceeded.");
        } catch (FunctionEvaluationException e) {
            fail("Error evaluating function.");
        }

    }

}
