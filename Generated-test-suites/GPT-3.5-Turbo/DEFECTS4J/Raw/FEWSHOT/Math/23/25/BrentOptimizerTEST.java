package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Create the BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Set the optimization parameters
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Call the optimize method
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check the result
        assertEquals(0, result.getPoint(), 0.0001);
        assertEquals(0, result.getValue(), 0.0001);
    }

    @Test
    public void testBest() {
        // Create the BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);

        // Create two UnivariatePointValuePair objects
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 1);

        // Call the best method
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        // Check the result
        assertEquals(a, result);
    }
}