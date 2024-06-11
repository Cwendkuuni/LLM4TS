import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() throws Exception {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer();

        // Create sample function and goal type
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Test optimize() method with startValue parameter
        double result = optimizer.optimize(f, goalType, -10, 10, 2);
        assertEquals(2.0, result, 0.00001);

        // Test optimize() method without startValue parameter
        result = optimizer.optimize(f, goalType, -10, 10);
        assertEquals(-2.0, result, 0.00001);
    }

    @Test
    public void testLocalMin() throws Exception {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer();

        // Create sample function and goal type
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Test localMin() method
        double result = optimizer.localMin(true, f, goalType, -10, 5, 10, 1e-10, 1e-14);
        assertEquals(1.0, result, 0.00001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinException() throws Exception {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer();

        // Create sample function and goal type
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Test localMin() method with invalid eps value
        optimizer.localMin(true, f, goalType, -10, 5, 10, -1, 1e-14);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinException2() throws Exception {
        // Create instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer();

        // Create sample function and goal type
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 3 * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Test localMin() method with invalid t value
        optimizer.localMin(true, f, goalType, -10, 5, 10, 1e-10, -1);
    }
}