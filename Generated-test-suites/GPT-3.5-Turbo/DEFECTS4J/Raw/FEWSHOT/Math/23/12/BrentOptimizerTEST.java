
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer1 = new BrentOptimizer(0.1, 0.01);
        optimizer1.setMin(1);
        optimizer1.setMax(10);
        optimizer1.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        assertEquals(1.0, result1.getPoint(), 0.01);
        assertEquals(1.0, result1.getValue(), 0.01);
        
        // Test case 2
        BrentOptimizer optimizer2 = new BrentOptimizer(0.1, 0.01);
        optimizer2.setMin(-10);
        optimizer2.setMax(0);
        optimizer2.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        assertEquals(0.0, result2.getPoint(), 0.01);
        assertEquals(0.0, result2.getValue(), 0.01);
        
        // Test case 3
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.001);
        optimizer3.setMin(-5);
        optimizer3.setMax(5);
        optimizer3.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result3 = optimizer3.doOptimize();
        assertEquals(0.0, result3.getPoint(), 0.01);
        assertEquals(0.0, result3.getValue(), 0.01);
    }
}
