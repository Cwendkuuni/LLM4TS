import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        // Test constructor with valid parameters
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimize() {
        // Test optimization with a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x, GoalType.MINIMIZE, 0, 10, 5);
        assertEquals(0, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidRel() {
        // Test constructor with invalid relative threshold
        new BrentOptimizer(1e-20, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidAbs() {
        // Test constructor with invalid absolute threshold
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testEdgeCasesOptimize() {
        // Test edge case optimization scenarios
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x, GoalType.MINIMIZE, 0, 10, -1);
        assertEquals(0, result.getPoint(), 1e-6);
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling for optimization
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> {
            if (x < 0) {
                throw new IllegalArgumentException("Input must be non-negative");
            }
            return x * x;
        }, GoalType.MINIMIZE, 0, 10, 5);
        assertEquals(0, result.getPoint(), 1e-6);
    }
}