import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testMinimumFinding() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-10);
        
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };
        
        double min = optimizer.optimize(function, GoalType.MINIMIZE, 1, 5).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeRelativeThreshold() {
        new BrentOptimizer(-1e-6, 1e-10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveAbsoluteThreshold() {
        new BrentOptimizer(1e-6, -1e-10);
    }
    
    @Test
    public void testConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-10, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return FastMath.abs(previous.getPoint() - current.getPoint()) < 1e-6;
            }
        });
        
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };
        
        double min = optimizer.optimize(function, GoalType.MINIMIZE, 1, 5).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-6);
    }
}