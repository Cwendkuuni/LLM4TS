import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test basic functionality
        BrentOptimizer optimizer = new BrentOptimizer(1e-7, 1e-9);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x - 4, GoalType.MINIMIZE);
        assertEquals(2.0, result.getPoint(), 1e-9);
    }

    @Test
    public void testBest() {
        // Test best method for minimizing values
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair bestResult = BrentOptimizer.best(a, b, true);
        assertEquals(b, bestResult);
    }

    @Test
    public void testEdgeCases() {
        // Test edge case where minimum is at lower bound
        BrentOptimizer optimizer = new BrentOptimizer(1e-7, 1e-9);
        UnivariatePointValuePair result = optimizer.optimize(0, x -> x * x, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-9);
        
        // Test edge case where minimum is at upper bound
        result = optimizer.optimize(10, x -> (x - 5) * (x - 5), GoalType.MINIMIZE);
        assertEquals(5.0, result.getPoint(), 1e-9);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsThreshold() {
        // Test case where absolute threshold is negative
        BrentOptimizer optimizer = new BrentOptimizer(1e-7, -1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallRelativeThreshold() {
        // Test case where relative threshold is too small
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-9);
    }

}