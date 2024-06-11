
package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.001);
        
        // Test case 1
        double[] startPoint1 = {1.0, 2.0, 3.0};
        double[] expected1 = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint1);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result1 = optimizer.doOptimize();
        assertEquals(expected1, result1.getPoint());

        // Test case 2
        double[] startPoint2 = {4.0, 5.0};
        double[] expected2 = {3.0, 3.0};
        optimizer.setStartPoint(startPoint2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result2 = optimizer.doOptimize();
        assertEquals(expected2, result2.getPoint());
        
        // Test case 3
        double[] startPoint3 = {-1.0, -2.0, -3.0};
        double[] expected3 = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint3);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result3 = optimizer.doOptimize();
        assertEquals(expected3, result3.getPoint());
        
        // Test case 4
        double[] startPoint4 = {0.0, 0.0};
        double[] expected4 = {1.0, 1.0};
        optimizer.setStartPoint(startPoint4);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result4 = optimizer.doOptimize();
        assertEquals(expected4, result4.getPoint());
    }
}
