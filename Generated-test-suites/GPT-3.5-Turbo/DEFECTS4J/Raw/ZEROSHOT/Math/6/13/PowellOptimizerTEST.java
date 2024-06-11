import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        PowellOptimizer optimizer1 = new PowellOptimizer(0.0001, 0.0001);
        // Set up inputs
        double[] startPoint1 = {1, 2, 3};
        double[][] vertices1 = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        double[] evaluations1 = {4, 5, 6};
        // Set expectations
        double[] expectedPoint1 = {1, 0, 0};
        double expectedValue1 = 4;
        // Run test
        PointValuePair result1 = optimizer1.doOptimize();
        double[] actualPoint1 = result1.getPoint();
        double actualValue1 = result1.getValue();
        // Assertions
        assertArrayEquals(expectedPoint1, actualPoint1);
        assertEquals(expectedValue1, actualValue1, 1e-15);
        
        // Test case 2
        PowellOptimizer optimizer2 = new PowellOptimizer(0.001, 0.001);
        // Set up inputs
        double[] startPoint2 = {10, 20, 30};
        double[][] vertices2 = {
            {10, 0, 0},
            {0, 20, 0},
            {0, 0, 30},
            {5, 5, 5}
        };
        double[] evaluations2 = {40, 50, 60, 30};
        // Set expectations
        double[] expectedPoint2 = {10, 0, 0};
        double expectedValue2 = 40;
        // Run test
        PointValuePair result2 = optimizer2.doOptimize();
        double[] actualPoint2 = result2.getPoint();
        double actualValue2 = result2.getValue();
        // Assertions
        assertArrayEquals(expectedPoint2, actualPoint2);
        assertEquals(expectedValue2, actualValue2, 1e-15);
    }
}