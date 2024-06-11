import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test for constructor with convergence checker
    @Test
    public void testConstructorWithChecker() {
        // Test valid input
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, checker);
        assertNotNull(optimizer);

        // Test invalid input
        try {
            BrentOptimizer optimizer2 = new BrentOptimizer(0.005, 0.001, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Test passed!
        }
    }

    // Test for constructor with defaults
    @Test
    public void testConstructorWithDefaults() {
        // Test valid input
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);

        // Test invalid input
        try {
            BrentOptimizer optimizer2 = new BrentOptimizer(0.005, -0.001);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Test passed!
        }
    }

    // Test for doOptimize method
    @Test
    public void testDoOptimize() {
        // Create a test BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);

        // Test doOptimize method with a simple example
        double min = optimizer.optimize(0, 5, x -> Math.pow(x - 2, 2)).getPoint();
        assertEquals(2.0, min, 0.0001);
    }

    // Additional tests for other methods in BrentOptimizer class

    // TODO: Add more test cases as necessary

}