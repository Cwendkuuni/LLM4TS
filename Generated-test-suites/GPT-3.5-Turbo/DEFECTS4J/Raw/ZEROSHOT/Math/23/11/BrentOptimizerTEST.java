import org.junit.Test;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testConstructorAbsNotStrictlyPositive() {
        new BrentOptimizer(1.0, -1.0);
    }
    
    @Test(expected=NumberIsTooSmallException.class)
    public void testConstructorRelTooSmall() {
        new BrentOptimizer(1e-16, 1.0);
    }
    
    @Test
    public void testDoOptimize() {
        // Create a BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        // Set the parameters of the optimizer
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Create a ConvergenceChecker object
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            
            public boolean converged(int iteration, UnivariatePointValuePair previousResult, UnivariatePointValuePair currentResult) {
                // Custom convergence criteria
                return iteration >= 5 && FastMath.abs(previousResult.getValue() - currentResult.getValue()) < 1e-8;
            }
        };
        
        // Set the convergence checker
        optimizer.setConvergenceChecker(checker);
        
        // Call the doOptimize method
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert statements
        Assert.assertTrue(Precision.equals(result.getValue(), 0, 1e-6));
        Assert.assertTrue(Precision.equals(result.getPoint(), 0, 1e-6));
    }
    
    @Test
    public void testBest() {
        // Create UnivariatePointValuePair objects
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        
        // Create a BrentOptimizer object
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        
        // Compare the two UnivariatePointValuePair objects using the best method
        UnivariatePointValuePair bestResult = optimizer.best(a, b, true);
        
        // Assert statements
        Assert.assertEquals(bestResult.getPoint(), 1);
        Assert.assertEquals(bestResult.getValue(), 1);
    }
}