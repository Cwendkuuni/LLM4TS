import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.doOptimize();
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -1, 1, 0);
            assertEquals(0, result, 1e-5);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 1;
            }
        };
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -1, 1);
            assertEquals(-1, result, 1e-5);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Additional tests for edge cases and exception handling can be added here

}