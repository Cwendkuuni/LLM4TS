import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer = null;
    
    public BrentOptimizerTest() {
        this.optimizer = new BrentOptimizer(1.0, 0.0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor() {
        optimizer = new BrentOptimizer(0.0, 0.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor2() {
        optimizer = new BrentOptimizer(1.0, -1.0);
    }
    
    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
        
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.doOptimize();
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNotNull(result);
        assertEquals(a, result);
        
        result = optimizer.best(a, b, false);
        assertNotNull(result);
        assertEquals(b, result);
        
        result = optimizer.best(null, b, true);
        assertNotNull(result);
        assertEquals(b, result);
        
        result = optimizer.best(a, null, true);
        assertNotNull(result);
        assertEquals(a, result);
        
        result = optimizer.best(null, null, true);
        assertNull(result);
    }
}