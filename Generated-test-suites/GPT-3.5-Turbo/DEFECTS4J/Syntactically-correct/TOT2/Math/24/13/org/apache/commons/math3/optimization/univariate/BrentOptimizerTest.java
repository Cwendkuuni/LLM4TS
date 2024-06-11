import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer brentOptimizer;

    @Before
    public void setUp() {
        brentOptimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    @Test
    public void testOptimize() {
        double min = 0.0;
        double max = 10.0;
        double result = brentOptimizer.optimize(100, (x) -> 2 * x - 5, min, max);

        assertEquals(2.5, result, 1e-5);
    }

    @Test
    public void testOptimizeWithInverseFunction() {
        double min = -10.0;
        double max = 0.0;
        double result = brentOptimizer.optimize(100, (x) -> -2 * x + 5, min, max);

        assertEquals(2.5, result, 1e-5);
    }

    @Test
    public void testOptimizeWithCustomFunction() {
        double min = -10.0;
        double max = 10.0;
        double result = brentOptimizer.optimize(100, (x) -> x * x - 4, min, max);

        assertEquals(2.0, result, 1e-5);
    }

    @Test
    public void testOptimizeWithInvalidRange() {
        double min = 10.0;
        double max = 0.0;

        try {
            brentOptimizer.optimize(100, (x) -> 2 * x - 5, min, max);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid range: min must be less than max", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithMaxIterationsReached() {
        double min = -10.0;
        double max = 10.0;

        try {
            brentOptimizer.optimize(1, (x) -> 2 * x - 5, min, max);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Max iterations reached", e.getMessage());
        }
    }
}