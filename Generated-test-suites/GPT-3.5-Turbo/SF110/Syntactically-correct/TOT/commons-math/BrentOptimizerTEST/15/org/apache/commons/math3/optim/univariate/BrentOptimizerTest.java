import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testBrentOptimizer_Constructor() {
        // Typical use case
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
        
        // Edge case: minimum relative tolerance
        BrentOptimizer optimizer2 = new BrentOptimizer(2.0 * Double.MIN_VALUE, 0.001);
        assertNotNull(optimizer2);
        
        // Error scenario: relative tolerance smaller than minimum
        try {
            BrentOptimizer optimizer3 = new BrentOptimizer(1E-15, 0.001);
            fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            assertEquals("The relative tolerance value is too small: 1.0E-15, minimum allowed: 4.9E-324", e.getMessage());
        }
        
        // Error scenario: non-positive absolute threshold
        try {
            BrentOptimizer optimizer4 = new BrentOptimizer(0.01, -0.001);
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("The absolute threshold must be positive but received: -0.001", e.getMessage());
        }
    }
    
    @Test
    public void testDoOptimize() {
        // Typical use case: minimize value within range [0, 10]
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals(0.0, optimizer.computeObjectiveValue(result.getPoint()), 0.001);
        
        // Typical use case: maximize value within range [-10, 0]
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001);
        optimizer2.setGoalType(GoalType.MAXIMIZE);
        optimizer2.setMin(-10);
        optimizer2.setMax(0);
        optimizer2.setStartValue(-5);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        assertEquals(0.0, result2.getValue(), 0.001);
        assertEquals(0.0, optimizer2.computeObjectiveValue(result2.getPoint()), 0.001);
        
        // Error scenario: invalid range [10, 0], expecting exception
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.001);
        optimizer3.setGoalType(GoalType.MINIMIZE);
        optimizer3.setMin(10);
        optimizer3.setMax(0);
        optimizer3.setStartValue(5);
        try {
            UnivariatePointValuePair result3 = optimizer3.doOptimize();
            fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("The range must be positive but received: [10, 0]", e.getMessage());
        }
    }
}