package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Creating a BrentOptimizer object with relative threshold set to 0.0001, absolute threshold set to 0.00001  and null convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.00001, null);
        // Setting the min, max, and starting values
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        // Creating a function to minimize
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        // Setting the function to minimize
        optimizer.setObjectiveFunction(function);

        // Optimize the function
        UnivariatePointValuePair result = optimizer.optimize();

        // Check if the result is correct
        assertEquals(0, result.getPoint(), 0.00001);
        assertEquals(0, result.getValue(), 0.00001);
    }

    @Test
    public void testBest() {
        // Creating a BrentOptimizer object with relative threshold set to 0.0001, absolute threshold set to 0.00001  and null convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.00001, null);

        // Creating two UnivariatePointValuePair objects with different values
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 20);

        // Selecting the best point using the best method
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        // Check if the result is correct
        assertEquals(a, result);
    }
}