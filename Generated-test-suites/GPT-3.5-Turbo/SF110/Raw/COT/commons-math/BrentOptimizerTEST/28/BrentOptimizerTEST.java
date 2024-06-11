import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }
    
    @Test
    public void testConstructorWithChecker() {
        // Arrange
        double rel = 1e-5;
        double abs = 1e-5;
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker<>();
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Assert
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }
    
    @Test
    public void testConstructorWithoutChecker() {
        // Arrange
        double rel = 1e-5;
        double abs = 1e-5;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        // Arrange
        double rel = -1e-5;
        double abs = 1e-5;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        // Arrange
        double rel = 1e-5;
        double abs = 0.0;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimizeMinimize() {
        // Arrange
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(10.0);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getValue() <= 0.0);
        assertTrue(result.getPoint() >= 0.0 && result.getPoint() <= 10.0);
    }
    
    @Test
    public void testDoOptimizeMaximize() {
        // Arrange
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(10.0);
        
        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getValue() >= 0.0);
        assertTrue(result.getPoint() >= 0.0 && result.getPoint() <= 10.0);
    }
    
    private static class MyConvergenceChecker<T extends UnivariatePointValuePair> implements ConvergenceChecker<T> {
        @Override
        public boolean converged(int iteration, T previous, T current) {
            return false;
        }
    }
}