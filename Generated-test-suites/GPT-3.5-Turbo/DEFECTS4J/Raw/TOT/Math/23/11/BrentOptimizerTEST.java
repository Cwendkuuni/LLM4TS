import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1: Typical use case
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);

        // Test case 2: Edge case
        optimizer = new BrentOptimizer(1e-10, 1e-10);
        result = optimizer.optimize(0, x -> x * x, GoalType.MINIMIZE);
        assertEquals(0.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-10);

        // Test case 3: Error scenario
        try {
            optimizer = new BrentOptimizer(1e-10, -1e-10);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (org.apache.commons.math3.exception.NotStrictlyPositiveException e) {
            // As expected
        }
    }
}