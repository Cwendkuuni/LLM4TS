import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Arrange
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.001, checker);
        
        // Assert
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutChecker() {
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.001);
        
        // Assert
        assertNotNull(optimizer);
    }

    // Add more test cases for other methods here

}