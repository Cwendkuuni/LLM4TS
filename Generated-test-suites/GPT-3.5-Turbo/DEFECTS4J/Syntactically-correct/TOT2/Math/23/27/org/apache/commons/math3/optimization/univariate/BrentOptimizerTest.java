import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerInvalidRelativeThreshold() {
        try {
            new BrentOptimizer(1.0e-15, 1e-6);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        try {
            new BrentOptimizer(1e-6, -1e-6);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
}