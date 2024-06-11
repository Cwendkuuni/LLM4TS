import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize_minimizeFunction_returnsMinimumValue() {
        // Given
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
        
        // When
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Then
        assertEquals(expectedMinimumValue, result.getValue(), delta);
    }
    
    @Test
    public void testDoOptimize_maximizeFunction_returnsMaximumValue() {
        // Given
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        // When
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Then
        assertEquals(expectedMaximumValue, result.getValue(), delta);
    }
    
    @Test
    public void testDoOptimize_invalidInterval_returnsNaN() {
        // Given
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
        optimizer.setMin(Double.POSITIVE_INFINITY);
        optimizer.setMax(Double.NEGATIVE_INFINITY);
        
        // When
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Then
        assertTrue(Double.isNaN(result.getValue()));
    }
    
    @Test
    public void testDoOptimize_convergenceChecker_returnsConvergedResult() {
        // Given
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previousResult, UnivariatePointValuePair currentResult) {
                return iteration >= 10;
            }
        };
        optimizer.setConvergenceChecker(checker);
        
        // When
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Then
        assertTrue(result.getValue() != null);
    }
}