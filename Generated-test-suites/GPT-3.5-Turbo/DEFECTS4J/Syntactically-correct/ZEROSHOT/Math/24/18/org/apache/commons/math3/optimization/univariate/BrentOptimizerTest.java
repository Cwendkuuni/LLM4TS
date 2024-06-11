import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Test result is not null
        assertNotNull(result);

        // Test result value is within expected range
        double minValue = result.getValue();
        assertTrue(minValue > Double.NEGATIVE_INFINITY && minValue < Double.POSITIVE_INFINITY);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.3);

        // Test best value is returned
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(b, best);

        // Test null values return null
        best = optimizer.best(null, null, true);
        assertNull(best);
    }
}