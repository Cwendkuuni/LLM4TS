
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        boolean isMinim = true;
        UnivariatePointValuePair best = optimizer.best(a, b, isMinim);
        assertEquals(a, best);
    }
}
