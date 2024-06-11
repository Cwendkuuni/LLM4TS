package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test constructor with relative and absolute thresholds and ConvergenceChecker
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < 0.0001;
            }
        });

        // Test constructor with relative and absolute thresholds only
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001);

        // Perform optimization and check the result
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.exp(-Math.abs(x));
            }
        };

        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        UnivariatePointValuePair result2 = optimizer2.optimize(function, -5.0, 5.0);

        assertEquals(result.getX(), result2.getX(), 0.001);
        assertEquals(result.getValue(), result2.getValue(), 0.001);

        // Test optimization with no initial guess
        UnivariatePointValuePair noInitialGuessResult = optimizer2.optimize(function, Double.NaN, Double.NaN);
        assertNotNull(noInitialGuessResult);
    }
}