import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithChecker() {
        GoalType goal = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1, checker);
        
        assertNotNull(optimizer);
        assertEquals(goal, optimizer.getGoalType());
        assertEquals(0.001, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.1, optimizer.getAbsoluteThreshold(), 0.001);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        
        // Add test cases to cover the doOptimize method
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        
        // Add test cases to cover the best method
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 0.1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.001, 0.0);
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
}