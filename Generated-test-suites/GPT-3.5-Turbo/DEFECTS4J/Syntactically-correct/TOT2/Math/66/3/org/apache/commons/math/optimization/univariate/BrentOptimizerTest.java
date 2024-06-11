import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testOptimizeWithSpecificStartValue() {
        // Typical use case
        // Test optimizing a function with a specific start value
        double result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0, 0.5);
        assertEquals(0.0, result, 1e-5);
        
        // Edge cases
        // Test when start value is at the lower bound
        result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0, 0.0);
        assertEquals(0.0, result, 1e-5);
        
        // Test when start value is at the upper bound
        result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0, 1.0);
        assertEquals(1.0, result, 1e-5);
        
        // Error scenarios
        // Test when function evaluation fails
        try {
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0, 0.5);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }
    
    @Test
    public void testOptimizeWithDefaultStartValue() {
        // Typical use case
        // Test optimizing a function with the default start value
        double result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.0, result, 1e-5);
        
        // Edge cases
        // Test when the interval is very small
        result = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0.0, 0.0001);
        assertEquals(0.0, result, 1e-5);
        
        // Error scenarios
        // Test when maximum iterations are exceeded
        try {
            optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, -1.0, 1.0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
    
    // Additional test methods for other public methods in BrentOptimizer
    
}

class TestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return x * (x - 1);
    }
}