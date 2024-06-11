import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        // Test constructor with valid arguments
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertEquals(0.01, optimizer.relativeThreshold, 0.001);
        assertEquals(0.001, optimizer.absoluteThreshold, 0.001);
        
        // Test constructor with invalid arguments
        try {
            optimizer = new BrentOptimizer(-0.01, 0.001);
        } catch (Exception e) {
            assertEquals(NumberIsTooSmallException.class, e.getClass());
        }
        
        try {
            optimizer = new BrentOptimizer(0.01, -0.001);
        } catch (Exception e) {
            assertEquals(NotStrictlyPositiveException.class, e.getClass());
        }
    }
    
    @Test
    public void testDoOptimize() {
        // Test case where goal type is MINIMIZE
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(3.0, result.getPoint(), 0.001);
        assertEquals(-9.0, result.getValue(), 0.001);
        
        // Test case where goal type is MAXIMIZE
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.doOptimize();
        assertEquals(7.0, result.getPoint(), 0.001);
        assertEquals(9.0, result.getValue(), 0.001);
    }
    
    @Test
    public void testBest() {
        // Test case where a and b are null
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNull(result);
        
        // Test case where a is null
        b = new UnivariatePointValuePair(3.0, -9.0);
        result = optimizer.best(a, b, true);
        assertEquals(3.0, result.getPoint(), 0.001);
        assertEquals(-9.0, result.getValue(), 0.001);
        
        // Test case where b is null
        a = new UnivariatePointValuePair(7.0, 9.0);
        result = optimizer.best(a, b, true);
        assertEquals(7.0, result.getPoint(), 0.001);
        assertEquals(9.0, result.getValue(), 0.001);
        
        // Test case where a and b are not null and isMinim is true
        a = new UnivariatePointValuePair(2.0, -5.0);
        b = new UnivariatePointValuePair(1.0, -8.0);
        result = optimizer.best(a, b, true);
        assertEquals(1.0, result.getPoint(), 0.001);
        assertEquals(-8.0, result.getValue(), 0.001);
        
        // Test case where a and b are not null and isMinim is false
        a = new UnivariatePointValuePair(2.0, -5.0);
        b = new UnivariatePointValuePair(1.0, -8.0);
        result = optimizer.best(a, b, false);
        assertEquals(2.0, result.getPoint(), 0.001);
        assertEquals(-5.0, result.getValue(), 0.001);
    }
}