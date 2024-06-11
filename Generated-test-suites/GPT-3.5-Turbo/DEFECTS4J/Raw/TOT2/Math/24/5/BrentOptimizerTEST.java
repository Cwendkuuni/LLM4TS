import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Typical Use Case
        // Test the optimization method on a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBest() {
        // Edge Case
        // Test selecting the best point between two given points
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, true);
        assertEquals(pointB, bestPoint);
    }

    @Test
    public void testConstructor() {
        // Error Scenario
        // Test constructor with negative relative threshold
        try {
            BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6);
            fail("Expected exception NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testComputeObjectiveValue() {
        // Edge Case
        // Test the computation of the objective value at a point
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double value = optimizer.computeObjectiveValue(2.0);
        assertEquals(0.0, value, 1e-6);
    }

    // Add more test cases for other public methods in BrentOptimizer if needed

}