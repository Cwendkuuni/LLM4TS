import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case for the doOptimize method

        // Create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-15, 1e-15);

        // Test a basic optimization scenario
        optimizer.optimize(0, 10, 5, GoalType.MINIMIZE);
        // Add assertions here
    }

    @Test
    public void testBest() {
        // Test case for the best method

        // Create two UnivariatePointValuePair instances
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 5);

        // Test when a is the best point
        UnivariatePointValuePair bestResult = optimizer.best(a, b, true);
        assertEquals(a, bestResult);

        // Test when b is the best point
        bestResult = optimizer.best(b, a, true);
        assertEquals(b, bestResult);
    }

    // Add more test cases for other public methods

}