import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Set up optimizer parameters
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Test case 1: minimum is within the range of [1, 10]
        double min1 = optimizer.optimize(100, x -> Math.pow(x - 5, 2)).getPoint();
        Assert.assertEquals(5.0, min1, 1e-6);
        
        // Test case 2: minimum is at the lower bound
        double min2 = optimizer.optimize(0, x -> Math.pow(x, 2)).getPoint();
        Assert.assertEquals(0.0, min2, 1e-6);
        
        // Test case 3: minimum is at the upper bound
        double min3 = optimizer.optimize(10, x -> Math.pow(x - 10, 2)).getPoint();
        Assert.assertEquals(10.0, min3, 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRel() {
        double rel = 1e-9;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbs() {
        double rel = 1e-6;
        double abs = 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
}