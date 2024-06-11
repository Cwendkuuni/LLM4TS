import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor_withValidRelAndAbs_shouldCreateObject() {
        // Arrange
        double rel = 0.1;
        double abs = 0.01;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withInvalidRel_shouldThrowNumberIsTooSmallException() {
        // Arrange
        double rel = -0.1;
        double abs = 0.01;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        // Exception is expected to be thrown
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidAbs_shouldThrowNotStrictlyPositiveException() {
        // Arrange
        double rel = 0.1;
        double abs = 0.0;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        // Exception is expected to be thrown
    }
    
    @Test
    public void testDoOptimize_withMinimizationGoalTypeAndValidRange_shouldReturnOptimalResult() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimize_withMaximizationGoalTypeAndValidRange_shouldReturnOptimalResult() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertEquals(10, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimize_withInvalidRange_shouldReturnOptimalResultAtBoundary() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(0);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimize_withConvergenceChecker_shouldReturnOptimalResult() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01, new MyConvergenceChecker());
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getPoint(), 0.001);
        assertEquals(0, result.getValue(), 0.001);
    }
    
    @Test
    public void testBest_withValidPointsAndMinimizationGoalType_shouldReturnPointWithSmallestValue() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);
        
        // Act
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPoint(), 0.001);
        assertEquals(5, result.getValue(), 0.001);
    }
    
    @Test
    public void testBest_withValidPointsAndMaximizationGoalType_shouldReturnPointWithLargestValue() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);
        
        // Act
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPoint(), 0.001);
        assertEquals(10, result.getValue(), 0.001);
    }
    
    // Add more test cases for other methods
    
}