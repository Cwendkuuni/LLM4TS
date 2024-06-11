import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        double actual = optimizer.doOptimize().getValue();
        
        assertEquals(0.0, actual, 1e-6);
    }
    
    @Test
    public void testDoOptimize_maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });
        
        double actual = optimizer.doOptimize().getValue();
        
        assertEquals(0.0, actual, 1e-6);
    }
}