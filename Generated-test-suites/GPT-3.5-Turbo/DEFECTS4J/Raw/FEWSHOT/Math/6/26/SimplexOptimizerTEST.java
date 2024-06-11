package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    // Helper method to build an optimization data array
    private OptimizationData[] buildOptimizationData(Object... data) {
        OptimizationData[] optData = new OptimizationData[data.length];
        for (int i = 0; i < data.length; i++) {
            optData[i] = (OptimizationData) data[i];
        }
        return optData;
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Define the optimization problem
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + 2 * point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData initialGuess = new org.apache.commons.math3.optimization.InitialGuess(new double[] {0, 0});

        // Optimize the function
        PointValuePair result = optimizer.optimize(100, f, goalType,
                buildOptimizationData(initialGuess));

        // Check the result
        double[] expectedPoint = {0, 0};
        double expectedValue = 0.0;

        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-15);
    }

    @Test
    public void testOptimizeInternal2() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        // Define the optimization problem
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + 2 * point[1] * point[1] + 3 * point[2] * point[2];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        OptimizationData initialGuess = new org.apache.commons.math3.optimization.InitialGuess(new double[] {0, 0, 0});

        // Optimize the function
        PointValuePair result = optimizer.optimize(100, f, goalType,
                buildOptimizationData(initialGuess));

        // Check the result
        double[] expectedPoint = {0, 0, 0};
        double expectedValue = 0.0;

        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-15);
    }
}