import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Typical use case
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        // Edge case - minimum at lower boundary
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        result = optimizer.doOptimize();
        assertEquals(-10.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        // Edge case - minimum at upper boundary
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        result = optimizer.doOptimize();
        assertEquals(10.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        // Error scenario - rel < 2 * Math.ulp(1d)
        try {
            optimizer = new BrentOptimizer(1e-10, 1e-6);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Exception expected
        }

        // Error scenario - abs <= 0
        try {
            optimizer = new BrentOptimizer(1e-6, 0.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
    }

}