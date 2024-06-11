
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5, checker);
        
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithCheckerInvalidRelativeThreshold() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyChecker();
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.5, checker);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithCheckerInvalidAbsoluteThreshold() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1.0, -0.5, checker);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithoutCheckerInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5, 0.5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithoutCheckerInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, -0.5);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(b, result);
    }
    
    class MyChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true; // Always return true for testing purposes
        }
    }
}
