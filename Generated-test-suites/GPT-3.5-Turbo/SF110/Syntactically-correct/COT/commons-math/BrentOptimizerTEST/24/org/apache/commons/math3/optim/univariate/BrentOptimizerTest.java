import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithConvergenceChecker() {
        // Arrange
        double rel = 0.0001;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairChecker();
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Assert
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Arrange
        double rel = 0.0001;
        double abs = 0.001;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        // Arrange
        double rel = -0.0001;
        double abs = 0.001;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        // Arrange
        double rel = 0.0001;
        double abs = 0.0;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    // Add more test cases to cover all the public methods and edge cases
    
}