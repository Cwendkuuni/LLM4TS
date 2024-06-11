
package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test input values
        double rel = 1e-6;
        double abs = 1e-8;

        double[] xValues = {0, 1, -1};
        GoalType[] goalTypes = {GoalType.MINIMIZE, GoalType.MAXIMIZE};

        // Expected output values
        double[] expectedMinimizationResults = {0, -1, -1};
        double[] expectedMaximizationResults = {0, 1, 1};

        // Run tests for all combinations of xValues and goalTypes
        for (double xValue : xValues) {
            for (GoalType goalType : goalTypes) {
                BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
                optimizer.setMin(xValue - 1);
                optimizer.setMax(xValue + 1);
                optimizer.setStartValue(xValue);
                optimizer.setGoalType(goalType);

                UnivariatePointValuePair result = optimizer.optimize();

                if (goalType == GoalType.MINIMIZE) {
                    assertEquals(expectedMinimizationResults[(int) xValue + 1], result.getPoint(), abs);
                } else {
                    assertEquals(expectedMaximizationResults[(int) xValue + 1], result.getPoint(), abs);
                }
            }
        }
    }
}
