
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer1 = new BrentOptimizer(0.01, 0.001);
        optimizer1.setMin(0.0);
        optimizer1.setMax(10.0);
        optimizer1.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result1 = optimizer1.optimize();
        assertEquals(0.0, result1.getPoint(), 0.001);
        
        // Test case 2
        BrentOptimizer optimizer2 = new BrentOptimizer(0.01, 0.001);
        optimizer2.setMin(-10.0);
        optimizer2.setMax(10.0);
        optimizer2.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result2 = optimizer2.optimize();
        assertEquals(-10.0, result2.getPoint(), 0.001);
        
        // Test case 3
        BrentOptimizer optimizer3 = new BrentOptimizer(0.01, 0.001);
        optimizer3.setMin(-10.0);
        optimizer3.setMax(-5.0);
        optimizer3.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result3 = optimizer3.optimize();
        assertEquals(-5.0, result3.getPoint(), 0.001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testException1() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.01, 0.001);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testException2() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, -0.001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        BrentOptimizer optimizerMock = new BrentOptimizer(0.01, 0.001);
        
        UnivariatePointValuePair best1 = optimizer.best(null, null, true);
        assertNull(best1);
        
        UnivariatePointValuePair best2 = optimizer.best(null, optimizerMock.new UnivariatePointValuePair(1.0, 10.0), true);
        assertEquals(1.0, best2.getPoint(), 0.001);
        
        UnivariatePointValuePair best3 = optimizer.best(optimizerMock.new UnivariatePointValuePair(-1.0, 10.0), optimizerMock.new UnivariatePointValuePair(1.0, 10.0), true);
        assertEquals(-1.0, best3.getPoint(), 0.001);
        
        UnivariatePointValuePair best4 = optimizer.best(optimizerMock.new UnivariatePointValuePair(-1.0, 10.0), optimizerMock.new UnivariatePointValuePair(1.0, 5.0), true);
        assertEquals(1.0, best4.getPoint(), 0.001);
    }
}

