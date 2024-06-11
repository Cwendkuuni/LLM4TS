import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        optimizer.setMin(1.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.618033988749895, result.getPoint(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_maximizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        optimizer.setMin(1.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.618033988749895, result.getPoint(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_minimizeGoalTypeWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-8, 1e-8));
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        optimizer.setMin(1.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.618033988749895, result.getPoint(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_maximizeGoalTypeWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new SimpleUnivariateValueChecker(1e-8, 1e-8));
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        optimizer.setMin(1.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(1.618033988749895, result.getPoint(), 1e-6);
    }
    
    @Test(expected=NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructor_relativeThresholdTooSmall() {
        new BrentOptimizer(0.0, 1e-6);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructor_absoluteThresholdNotStrictlyPositive() {
        new BrentOptimizer(1e-6, 0.0);
    }

}