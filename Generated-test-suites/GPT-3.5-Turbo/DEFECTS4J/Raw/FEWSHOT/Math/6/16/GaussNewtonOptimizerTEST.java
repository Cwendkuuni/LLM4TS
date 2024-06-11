package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Set up the test data
        double[] target = {1.0, 2.0, 3.0};
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};

        // Create a GaussNewtonOptimizer instance
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Optimize the target values
        PointVectorValuePair result = optimizer.doOptimize(target, weight, startPoint);

        // Assert the optimized values are correct
        double[] expectedPoint = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);

        // Assert the cost value is correct
        double expectedCost = 0.0;
        assertEquals(expectedCost, result.getValue(), 0.0001);
    }

}