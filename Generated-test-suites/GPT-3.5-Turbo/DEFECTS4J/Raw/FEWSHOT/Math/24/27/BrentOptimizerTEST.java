import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;



public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
        
        double min = -10.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        
        UnivariatePointValuePair result = optimizer.optimize(100, new UnivariatePointValuePair(min, 0), goalType, min, max);
        assertEquals(0.0, result.getPoint(), 0.0001);
        
        goalType = GoalType.MAXIMIZE;
        
        result = optimizer.optimize(100, new UnivariatePointValuePair(min, 0), goalType, min, max);
        assertEquals(0.0, result.getPoint(), 0.0001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, null);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 20);
        
        assertTrue(optimizer.best(a, b, true) == a);
        assertTrue(optimizer.best(a, b, false) == b);
    }
}