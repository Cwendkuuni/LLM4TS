import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0, null);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Absence of improvement tolerance must be positive ({0})", e.getMessage());
        }

        try {
            BrentOptimizer optimizer = new BrentOptimizer(2.0 * FastMath.ulp(1.0), 1.0, null);
        } catch (NumberIsTooSmallException e) {
            assertEquals("2 * ULP(1.0) is smaller than the minimum allowed (2.0E-15)", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithoutChecker() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Absence of improvement tolerance must be positive ({0})", e.getMessage());
        }

        try {
            BrentOptimizer optimizer = new BrentOptimizer(2.0 * FastMath.ulp(1.0), 1.0);
        } catch (NumberIsTooSmallException e) {
            assertEquals("2 * ULP(1.0) is smaller than the minimum allowed (2.0E-15)", e.getMessage());
        }
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-15, 1e-15);

        double result = optimizer.doOptimize();

        double expected = 0.5 * (3.0 - FastMath.sqrt(5.0)) * Math.sqrt(5.0);

        assertEquals(expected, result, 1e-15);
    }
}