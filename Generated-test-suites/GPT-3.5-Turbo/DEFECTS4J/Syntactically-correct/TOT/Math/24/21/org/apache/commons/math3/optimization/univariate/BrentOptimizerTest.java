import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_typicalCase() {
        double rel = 1e-4;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Check that the optimizer is instantiated correctly
        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_relTooSmall() {
        double rel = 1e-10;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_absNotStrictlyPositive() {
        double rel = 1e-4;
        double abs = -1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testDoOptimize_typicalCase() {
        double rel = 1e-4;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set up test case
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Perform optimization
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Check that the optimization result is correct
        Assert.assertEquals(1.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_edgeCase() {
        double rel = 1e-4;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set up test case
        optimizer.setMin(0.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Perform optimization
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Check that the optimization result is correct
        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize_errorCase() {
        double rel = 1e-4;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set up test case
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // Perform optimization
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Check that the optimization result is correct
        Assert.assertEquals(0.0, result.getPoint(), 0.0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.0);
    }

}