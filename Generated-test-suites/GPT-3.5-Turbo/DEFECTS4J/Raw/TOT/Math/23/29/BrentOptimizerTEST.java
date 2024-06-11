import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        optimizer = new BrentOptimizer(1e-10, 0, checker);
    }
    
    @Test
    public void testDoOptimize_minimize() {
        double min = optimizer.optimize(100, x -> Math.pow((x - 5), 2), GoalType.MINIMIZE).getPoint();
        Assert.assertEquals(5, min, 1e-10);
    }
    
    @Test
    public void testDoOptimize_maximize() {
        double max = optimizer.optimize(100, x -> Math.pow((10 - x), 2), GoalType.MAXIMIZE).getPoint();
        Assert.assertEquals(10, max, 1e-10);
    }
    
    @Test
    public void testDoOptimize_boundaryPoints() {
        double minBoundary = optimizer.optimize(0, x -> Math.abs(x), GoalType.MINIMIZE).getPoint();
        double maxBoundary = optimizer.optimize(0, x -> Math.abs(x), GoalType.MAXIMIZE).getPoint();
        Assert.assertEquals(0, minBoundary, 1e-10);
        Assert.assertEquals(0, maxBoundary, 1e-10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDoOptimize_invalidAbsThreshold() {
        optimizer = new BrentOptimizer(1e-10, -1);
        optimizer.optimize(100, x -> Math.pow(x, 2), GoalType.MINIMIZE);
    }
    
    @Test
    public void testDoOptimize_relativeThresholdSmallerThanMinimum() {
        double result = optimizer.optimize(100, x -> Math.pow(x, 2), GoalType.MINIMIZE).getPoint();
        Assert.assertEquals(0, result, 1e-10);
    }

}