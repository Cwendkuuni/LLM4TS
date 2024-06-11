
package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Set up the optimizer
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Set up the objective function
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return Math.pow(x, 2) + Math.pow(y, 2); // Example objective function: x^2 + y^2
            }
        };

        // Set up the start point and goal type
        double[] startPoint = {1, 1};
        GoalType goalType = GoalType.MINIMIZE;

        // Optimize the function
        PointValuePair result = optimizer.optimize(100, objectiveFunction, goalType, startPoint);

        // Verify the result
        double[] expectedPoint = {0, 0};
        double expectedValue = 0;
        assertArrayEquals(expectedPoint, result.getPoint(), 0);
        assertEquals(expectedValue, result.getValue(), 0);
    }
}
