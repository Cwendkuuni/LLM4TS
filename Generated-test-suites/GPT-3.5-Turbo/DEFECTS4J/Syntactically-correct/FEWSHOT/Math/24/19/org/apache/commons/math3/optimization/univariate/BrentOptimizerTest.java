package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testDoOptimize() {
        // Create BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-5);

        // Create test objective function
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - x - 6;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = 5;

        // Set the parameters of the optimizer
        optimizer.setObjectiveFunction(objectiveFunction);
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setMax(max);

        // Run optimization
        UnivariatePointValuePair result = optimizer.optimize();

        // Check the result
        double expectedResult = 3;
        assertEquals(expectedResult, result.getPoint(), EPSILON);
    }

}