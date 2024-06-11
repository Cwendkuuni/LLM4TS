import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        
        // Typical use case
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x - 4, BrentOptimizer.GoalType.MINIMIZE, 0, 5);
        assertEquals(2.0, result.getPoint(), 1e-10);
        
        // Edge case where the function has multiple minima
        result = optimizer.optimize(100, x -> Math.abs(x) - 5, BrentOptimizer.GoalType.MINIMIZE, -10, 10);
        assertEquals(-5.0, result.getPoint(), 1e-10);
        
        // Error scenario where the function is not defined in the interval
        try {
            result = optimizer.optimize(100, x -> 1 / x, BrentOptimizer.GoalType.MINIMIZE, -1, 1);
            fail("Exception expected");
        } catch (ArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Testing with typical values
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(3.0, 9.0);

        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, BrentOptimizer.GoalType.MINIMIZE);
        assertEquals(pointA, bestPoint);

        // Testing with edge case where points have equal function values
        pointA = new UnivariatePointValuePair(2.0, 4.0);
        pointB = new UnivariatePointValuePair(3.0, 4.0);

        bestPoint = optimizer.best(pointA, pointB, BrentOptimizer.GoalType.MINIMIZE);
        assertEquals(pointA, bestPoint);
    }
}