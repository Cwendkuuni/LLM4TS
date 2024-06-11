
package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        // Replace f with your own UnivariateRealFunction implementation
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x; // Example function
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 1);
            assertEquals(0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        // Replace f with your own UnivariateRealFunction implementation
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x; // Example function
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 2, 1);
            assertEquals(0, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

}
