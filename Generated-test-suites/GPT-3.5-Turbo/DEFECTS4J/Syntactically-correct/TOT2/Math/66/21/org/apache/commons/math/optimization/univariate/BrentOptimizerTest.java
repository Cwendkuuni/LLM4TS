import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_StartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // Example function x^2 - 4
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5, 2);
            assertEquals(2.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testOptimize_NoStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // Example function x^2 - 4
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
            assertEquals(2.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testOptimize_Exception() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 3) {
                    throw new RuntimeException("Error evaluating function at x=3");
                }
                return x * x - 4; // Example function x^2 - 4
            }
        };
        try {
            double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
            fail("Exception expected but not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    // Add more test cases for other methods if needed

}