import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new SimpleValueChecker());
        double min = optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, 0, 10).getPoint();
        double max = optimizer.optimize(100, x -> -Math.pow(x - 2, 2), GoalType.MAXIMIZE, 0, 10).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-10);
        Assert.assertEquals(2.0, max, 1e-10);
    }
    
    @Test
    public void testOptimizeWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new SimpleValueChecker());
        double min = optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, 0, 10).getPoint();
        double max = optimizer.optimize(100, x -> -Math.pow(x - 2, 2), GoalType.MAXIMIZE, 0, 10).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-10);
        Assert.assertEquals(2.0, max, 1e-10);
    }
    
    @Test
    public void testOptimizeWithNoConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        double min = optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, 0, 10).getPoint();
        double max = optimizer.optimize(100, x -> -Math.pow(x - 2, 2), GoalType.MAXIMIZE, 0, 10).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-10);
        Assert.assertEquals(2.0, max, 1e-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidRange() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new SimpleValueChecker());
        optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, 10, 0);
    }
    
    @Test
    public void testOptimizeWithZeroRange() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new SimpleValueChecker());
        double min = optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, 0, 0).getPoint();
        double max = optimizer.optimize(100, x -> -Math.pow(x - 2, 2), GoalType.MAXIMIZE, 0, 0).getPoint();
        
        Assert.assertEquals(0.0, min, 1e-10);
        Assert.assertEquals(0.0, max, 1e-10);
    }
    
    @Test
    public void testOptimizeWithLargeRange() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, new SimpleValueChecker());
        double min = optimizer.optimize(100, x -> Math.pow(x - 2, 2), GoalType.MINIMIZE, -Double.MAX_VALUE, Double.MAX_VALUE).getPoint();
        double max = optimizer.optimize(100, x -> -Math.pow(x - 2, 2), GoalType.MAXIMIZE, -Double.MAX_VALUE, Double.MAX_VALUE).getPoint();
        
        Assert.assertEquals(2.0, min, 1e-10);
        Assert.assertEquals(2.0, max, 1e-10);
    }
}