import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize_WithTargetValues_CorrectResult() {
        // Create instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        // Set target values
        double[] targetValues = {1.0, 2.0, 3.0};
        optimizer.setTarget(targetValues);
        
        // Set start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);
        
        // Run doOptimize method
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Check if the result is correct based on input
        double[] expectedPoint = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_WithInvalidTargetValues_ThrowConvergenceException() {
        // Create instance of GaussNewtonOptimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        // Set target values (invalid, empty array)
        double[] targetValues = {};
        optimizer.setTarget(targetValues);
        
        // Set start point
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setStartPoint(startPoint);
        
        // Try running doOptimize method
        try {
            optimizer.doOptimize();
            fail("Expected ConvergenceException was not thrown");
        } catch (ConvergenceException e) {
            // Expected exception thrown, test passed
            assertTrue(true);
        }
    }
    
    // Add more test cases for other public methods and edge cases
    
}