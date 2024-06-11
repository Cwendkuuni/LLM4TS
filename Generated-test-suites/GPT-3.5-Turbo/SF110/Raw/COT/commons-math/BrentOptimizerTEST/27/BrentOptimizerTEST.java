
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(0.001, 0.0001);
    }
    
    @Test
    public void testDoOptimize() {
        // Test case for normal optimization
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
        
        // Test case for optimization with different goal type
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.doOptimize();
        assertEquals(10.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
        
        // Test case for optimization with a different start value
        optimizer.setStartValue(2);
        result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        // Test case for minimizing goal type
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
        
        // Test case for maximizing goal type
        result = optimizer.best(a, b, false);
        assertEquals(b, result);
    }
    
}
