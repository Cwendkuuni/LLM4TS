import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        // Test optimizing with start value
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.0;
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        assertEquals(2.0, result, 0.01);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        // Test optimizing without start value
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 2.0;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(2.0, result, 0.01);
    }

    // Add more test cases for edge cases and exceptions 

}