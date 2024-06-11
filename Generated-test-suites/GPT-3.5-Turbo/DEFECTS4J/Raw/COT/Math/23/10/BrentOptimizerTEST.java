import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create test instance of BrentOptimizer class
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001, null);
        
        // Test case for doOptimize method
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert statements to test the result
        assertNotNull(result);
        assertEquals(x, result.getPoint(), 0.0001);
        assertEquals(fx, result.getValue(), 0.0001);
    }

    @Test
    public void testBest() {
        // Create test instances of UnivariatePointValuePair class
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        // Test case for best method with isMinim = true
        UnivariatePointValuePair bestResult = BrentOptimizer.best(a, b, true);

        // Assert statements to test the result
        assertNotNull(bestResult);
        assertEquals(a.getPoint(), bestResult.getPoint(), 0.0001);
        assertEquals(a.getValue(), bestResult.getValue(), 0.0001);

        // Test case for best method with isMinim = false
        bestResult = BrentOptimizer.best(a, b, false);

        // Assert statements to test the result
        assertNotNull(bestResult);
        assertEquals(b.getPoint(), bestResult.getPoint(), 0.0001);
        assertEquals(b.getValue(), bestResult.getValue(), 0.0001);
    }
}