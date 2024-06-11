import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        
        // Test case 1
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001);
        double[] startPoint1 = {1, 2, 3};
        GoalType goalType1 = GoalType.MINIMIZE;
        double[] expectedResult1 = {0.999969482421875, 1.999969482421875, 2.999969482421875};
        PointValuePair expectedPointValuePair1 = new PointValuePair(expectedResult1, 0);
        PointValuePair actualPointValuePair1 = optimizer.doOptimize(startPoint1, goalType1);
        assertEquals(expectedPointValuePair1, actualPointValuePair1);
        
        // Test case 2
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001);
        double[] startPoint2 = {6, 5, 3, 2};
        GoalType goalType2 = GoalType.MAXIMIZE;
        double[] expectedResult2 = {3.0911865234375, 2.0911865234375, 0.0911865234375, 1.0911865234375};
        PointValuePair expectedPointValuePair2 = new PointValuePair(expectedResult2, 121100.193472);
        PointValuePair actualPointValuePair2 = optimizer.doOptimize(startPoint2, goalType2);
        assertEquals(expectedPointValuePair2, actualPointValuePair2);
        
        // Test case 3
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001);
        double[] startPoint3 = {-1, -2};
        GoalType goalType3 = GoalType.MINIMIZE;
        double[] expectedResult3 = {-0.90972900390625, -1.90972900390625};
        PointValuePair expectedPointValuePair3 = new PointValuePair(expectedResult3, 6.7588122119140625);
        PointValuePair actualPointValuePair3 = optimizer.doOptimize(startPoint3, goalType3);
        assertEquals(expectedPointValuePair3, actualPointValuePair3);
        
        // Test case 4
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001);
        double[] startPoint4 = {0.5, 1, 2};
        GoalType goalType4 = GoalType.MAXIMIZE;
        double[] expectedResult4 = {0.5115966796875, 1.5115966796875, 2.5115966796875};
        PointValuePair expectedPointValuePair4 = new PointValuePair(expectedResult4, 11.322509765625);
        PointValuePair actualPointValuePair4 = optimizer.doOptimize(startPoint4, goalType4);
        assertEquals(expectedPointValuePair4, actualPointValuePair4);
    }
    
}