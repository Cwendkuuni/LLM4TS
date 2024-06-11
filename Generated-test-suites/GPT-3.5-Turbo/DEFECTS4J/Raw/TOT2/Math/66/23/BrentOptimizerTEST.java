import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test when doOptimize is called, UnsupportedOperationException is thrown
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Test passed
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        // Test optimizing a function with start value provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> x * x, GoalType.MINIMIZE, -5, 5, 0);
        assertEquals(0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        // Test optimizing a function without start value provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(x -> Math.sin(x), GoalType.MAXIMIZE, 0, Math.PI);
        assertEquals(Math.PI / 2, result, 1e-10);
    }

    @Test
    public void testLocalMin() {
        // Test the localMin method with various scenarios
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.localMin(true, x -> x * x, GoalType.MINIMIZE, -5, 0, 5, 1e-10, 1e-14);
            assertEquals(0, result, 1e-10);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidEps() {
        // Test the localMin method with invalid epsilon
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x * x, GoalType.MINIMIZE, -5, 0, 5, 0, 1e-14);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidT() {
        // Test the localMin method with invalid t
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> x * x, GoalType.MINIMIZE, -5, 0, 5, 1e-10, 0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testLocalMinWithMaxIterationsExceeded() {
        // Test the localMin method where maximum iterations are exceeded
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, x -> Math.sin(x), GoalType.MAXIMIZE, 0, Math.PI, 2 * Math.PI, 1e-10, 1e-14);
    }
}