package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return (x - 2) * (x - 2);
            }
        };
        double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 5, 1);
        double expected = 2;
        assertEquals(expected, result, 0.001);
    }
}