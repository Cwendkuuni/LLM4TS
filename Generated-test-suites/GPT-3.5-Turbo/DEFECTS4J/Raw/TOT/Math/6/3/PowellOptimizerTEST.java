import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Test case 1: Minimum input values
        double[] startPoint1 = {0};
        double[] expectedSolution1 = {0};
        double expectedOptimalValue1 = 0;
        PowellOptimizer optimizer1 = new PowellOptimizer(1e-10, 1e-10);
        PointValuePair result1 = optimizer1.optimize(startPoint1);
        assertArrayEquals(expectedSolution1, result1.getPoint(), 1e-10);
        assertEquals(expectedOptimalValue1, result1.getValue(), 1e-10);

        // Test case 2: One-dimensional input with a non-zero start point
        double[] startPoint2 = {10};
        double[] expectedSolution2 = {0};
        double expectedOptimalValue2 = 0;
        PowellOptimizer optimizer2 = new PowellOptimizer(1e-10, 1e-10);
        PointValuePair result2 = optimizer2.optimize(startPoint2);
        assertArrayEquals(expectedSolution2, result2.getPoint(), 1e-10);
        assertEquals(expectedOptimalValue2, result2.getValue(), 1e-10);

        // Test case 3: Two-dimensional input with a non-zero start point
        double[] startPoint3 = {10, 20};
        double[] expectedSolution3 = {0, 0};
        double expectedOptimalValue3 = 0;
        PowellOptimizer optimizer3 = new PowellOptimizer(1e-10, 1e-10);
        PointValuePair result3 = optimizer3.optimize(startPoint3);
        assertArrayEquals(expectedSolution3, result3.getPoint(), 1e-10);
        assertEquals(expectedOptimalValue3, result3.getValue(), 1e-10);
    }
}