package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PowellOptimizerTest {
    @Test
    public void testDoOptimize() {
        // Create the optimizer with custom convergence checker
        double rel = 1e-4;
        double abs = 1e-6;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Your custom convergence check logic goes here
                return false;
            }
        });

        // Set the start point and goal type
        double[] startPoint = {1.0, 2.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Optimize and get the result
        PointValuePair result = optimizer.optimize();

        // Validate the result
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
        assertEquals(2, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}