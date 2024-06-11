import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithInvalidAbs() {
        try {
            new BrentOptimizer(1.0, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
    
    @Test
    public void testConstructorWithInvalidRel() {
        try {
            new BrentOptimizer(1.0, 0.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }
    
    @Test
    public void testOptimizeWithMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        
        UnivariatePointValuePair result = optimizer.optimize(100, 
                new ObjectiveFunction(x -> (x - 5) * (x - 5)), GoalType.MINIMIZE);
        
        assertEquals(5.0, result.getPoint(), 0.01);
    }
    
    @Test
    public void testOptimizeWithMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        
        UnivariatePointValuePair result = optimizer.optimize(100, 
                new ObjectiveFunction(x -> -(x - 5) * (x - 5)), GoalType.MAXIMIZE);
        
        assertEquals(5.0, result.getPoint(), 0.01);
    }
    
    // Add more test cases for different scenarios 
    
}