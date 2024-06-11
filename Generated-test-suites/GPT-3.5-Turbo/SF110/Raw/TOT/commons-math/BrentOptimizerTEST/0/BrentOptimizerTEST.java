import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithValidArguments() {
        double rel = 0.001;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = -0.001;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.001;
        double abs = -0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }
    
    @Test
    public void testConstructorWithPartialArguments() {
        double rel = 0.001;
        double abs = 0.001;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testDoOptimizeWithMinimizeGoalType() {
        double min = -10.0;
        double start = 0.0;
        double max = 10.0;
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, checker);
        optimizer.setMin(min);
        optimizer.setStartValue(start);
        optimizer.setMax(max);
        optimizer.setGoalType(goalType);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(start, result.getPoint(), 0.0);
        assertTrue(result.getValue() >= 0.0);
    }
    
    @Test
    public void testDoOptimizeWithMaximizeGoalType() {
        double min = -10.0;
        double start = 0.0;
        double max = 10.0;
        GoalType goalType = GoalType.MAXIMIZE;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001, checker);
        optimizer.setMin(min);
        optimizer.setStartValue(start);
        optimizer.setMax(max);
        optimizer.setGoalType(goalType);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(start, result.getPoint(), 0.0);
        assertTrue(result.getValue() <= 0.0);
    }
}