import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test that the BrentOptimizer optimizes correctly
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-9);
        UnivariatePointValuePair result = optimizer.optimize(100, new MyUnivariateObjectiveFunction(), GoalType.MINIMIZE,
            0, 10, 5);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-9);
    }

    @Test
    public void testBest() {
        // Test the best method with two different points
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 5.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-9);
        UnivariatePointValuePair result = optimizer.best(pointA, pointB, true);
        assertEquals(pointB, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidRel() {
        // Test constructor with invalid rel value
        new BrentOptimizer(1e-6, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidAbs() {
        // Test constructor with invalid abs value
        new BrentOptimizer(1e-5, -1e-10);
    }

}