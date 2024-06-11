import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertNotNull(result);
        assertEquals(b, result);
    }

}