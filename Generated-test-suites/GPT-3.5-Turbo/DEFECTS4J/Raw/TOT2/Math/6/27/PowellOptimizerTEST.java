package org.apache.commons.math3.optimization.direct;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        double[] startPoint = {0.0, 0.0};
        GoalType goalType = GoalType.MINIMIZE;

        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(goalType);

        PointValuePair result = optimizer.optimize();

        assertNotNull(result);
        assertEquals(0.0, result.getPoint()[0], 1e-5);
        assertEquals(0.0, result.getPoint()[1], 1e-5);
    }

    @Test
    public void testDoOptimizeWithInvalidStartingPoint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        double[] startPoint = {1.0}; // Invalid starting point

        optimizer.setStartPoint(startPoint);

        try {
            optimizer.optimize();
            fail("Expected IllegalArgumentException for invalid starting point");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testDoOptimizeWithInvalidGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        double[] startPoint = {0.0, 0.0};
        GoalType goalType = null; // Invalid goal type

        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(goalType);

        try {
            optimizer.optimize();
            fail("Expected NullPointerException for invalid goal type");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    // Add more test cases for other methods if needed

}