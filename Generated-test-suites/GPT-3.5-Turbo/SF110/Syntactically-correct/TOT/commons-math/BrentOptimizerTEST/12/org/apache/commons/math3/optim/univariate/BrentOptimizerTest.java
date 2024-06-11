import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        // Typical use case
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
        
        // Edge case: Minimum relative tolerance
        try {
            optimizer = new BrentOptimizer(-0.001, 0.01);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Pass
        }
        
        // Edge case: Absolute threshold equal to 0
        try {
            optimizer = new BrentOptimizer(0.001, 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        }
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        
        // Typical use case
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-5);
        
        // Error scenario: GoalType not set
        try {
            optimizer.setGoalType(null);
            optimizer.doOptimize();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 5.0);

        // Typical use case: isMinim = true
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        assertNotNull(result1);
        assertEquals(1.0, result1.getPoint(), 1e-5);
        assertEquals(5.0, result1.getValue(), 1e-5);

        // Typical use case: isMinim = false
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        assertNotNull(result2);
        assertEquals(0.0, result2.getPoint(), 1e-5);
        assertEquals(10.0, result2.getValue(), 1e-5);

        // Edge case: a is null
        UnivariatePointValuePair result3 = optimizer.best(null, b, true);
        assertNotNull(result3);
        assertEquals(1.0, result3.getPoint(), 1e-5);
        assertEquals(5.0, result3.getValue(), 1e-5);
        
        // Edge case: b is null
        UnivariatePointValuePair result4 = optimizer.best(a, null, true);
        assertNotNull(result4);
        assertEquals(0.0, result4.getPoint(), 1e-5);
        assertEquals(10.0, result4.getValue(), 1e-5);
    }
}