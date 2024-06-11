import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Typical use case
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
        UnivariatePointValuePair result = optimizer.optimize(100, (x) -> x * x);
        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
        
        // Edge case: minimum at the lower bound
        optimizer = new BrentOptimizer(1e-12, 1e-12);
        result = optimizer.optimize(100, (x) -> x * x, -10, 10);
        assertEquals(-10.0, result.getPoint(), 1e-12);
        assertEquals(100.0, result.getValue(), 1e-12);
        
        // Edge case: minimum at the upper bound
        optimizer = new BrentOptimizer(1e-12, 1e-12);
        result = optimizer.optimize(100, (x) -> -x * x, -10, 10);
        assertEquals(10.0, result.getPoint(), 1e-12);
        assertEquals(-100.0, result.getValue(), 1e-12);
        
        // Error scenario: invalid relative tolerance
        try {
            optimizer = new BrentOptimizer(-1e-12, 1e-12);
            result = optimizer.optimize(100, (x) -> x);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Exception expected
        }
        
        // Error scenario: invalid absolute tolerance
        try {
            optimizer = new BrentOptimizer(1e-12, -1e-12);
            result = optimizer.optimize(100, (x) -> x);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception expected
        }
    }

}