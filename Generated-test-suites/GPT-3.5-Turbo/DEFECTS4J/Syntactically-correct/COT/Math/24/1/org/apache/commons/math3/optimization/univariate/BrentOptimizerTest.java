import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case for doOptimize method
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Test assertions
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0);
        assertEquals(0, result.getValue(), 0);
    }

    @Test
    public void testBest() {
        // Test case for best method
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        // Test assertions
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0);
        assertEquals(0, result.getValue(), 0);
    }

}