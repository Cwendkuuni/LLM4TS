import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PowellOptimizerTest {
    private static final double EPSILON = 1e-6;
    
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        
        // Test case 1
        double[] startPoint1 = {1, 2};
        double[] lowerBound1 = {-10, -10};
        double[] upperBound1 = {10, 10};
        optimizer.setStartPoint(startPoint1);
        optimizer.setLowerBound(lowerBound1);
        optimizer.setUpperBound(upperBound1);
        PointValuePair result1 = optimizer.doOptimize();
        double[] expected1 = {0, 0};
        assertEquals(expected1[0], result1.getPoint()[0], EPSILON);
        assertEquals(expected1[1], result1.getPoint()[1], EPSILON);
        
        // Test case 2
        double[] startPoint2 = {0.1, 0.2};
        double[] lowerBound2 = {-1, -1};
        double[] upperBound2 = {1, 1};
        optimizer.setStartPoint(startPoint2);
        optimizer.setLowerBound(lowerBound2);
        optimizer.setUpperBound(upperBound2);
        PointValuePair result2 = optimizer.doOptimize();
        double[] expected2 = {0, 0};
        assertEquals(expected2[0], result2.getPoint()[0], EPSILON);
        assertEquals(expected2[1], result2.getPoint()[1], EPSILON);
    }
}