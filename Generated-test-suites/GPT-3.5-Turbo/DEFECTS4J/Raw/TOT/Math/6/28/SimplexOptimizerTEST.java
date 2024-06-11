import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + 2 * point[1] + 3;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false; // This is just a mock checker for testing purposes
            }
        };
        
        // Test optimization with GoalType.MINIMIZE
        PointValuePair resultMinimize = optimizer.optimizeInternal(maxEval, f, GoalType.MINIMIZE, new OptimizationData[0]);
        Assert.assertNotNull(resultMinimize);
        Assert.assertEquals(2, resultMinimize.getPoint().length);
        Assert.assertEquals(0, resultMinimize.getPoint()[0], 0);
        Assert.assertEquals(-1, resultMinimize.getPoint()[1], 0);
        Assert.assertEquals(3, resultMinimize.getValue(), 0);
        
        // Test optimization with GoalType.MAXIMIZE
        PointValuePair resultMaximize = optimizer.optimizeInternal(maxEval, f, GoalType.MAXIMIZE, new OptimizationData[0]);
        Assert.assertNotNull(resultMaximize);
        Assert.assertEquals(2, resultMaximize.getPoint().length);
        Assert.assertEquals(0, resultMaximize.getPoint()[0], 0);
        Assert.assertEquals(-1, resultMaximize.getPoint()[1], 0);
        Assert.assertEquals(3, resultMaximize.getValue(), 0);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + 2 * point[1] + 3;
            }
        };
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false; // This is just a mock checker for testing purposes
            }
        };
        
        // Test optimization with GoalType.MINIMIZE
        PointValuePair resultMinimize = optimizer.doOptimize(maxEval, f, GoalType.MINIMIZE, new OptimizationData[0]);
        Assert.assertNotNull(resultMinimize);
        Assert.assertEquals(2, resultMinimize.getPoint().length);
        Assert.assertEquals(0, resultMinimize.getPoint()[0], 0);
        Assert.assertEquals(-1, resultMinimize.getPoint()[1], 0);
        Assert.assertEquals(3, resultMinimize.getValue(), 0);
        
        // Test optimization with GoalType.MAXIMIZE
        PointValuePair resultMaximize = optimizer.doOptimize(maxEval, f, GoalType.MAXIMIZE, new OptimizationData[0]);
        Assert.assertNotNull(resultMaximize);
        Assert.assertEquals(2, resultMaximize.getPoint().length);
        Assert.assertEquals(0, resultMaximize.getPoint()[0], 0);
        Assert.assertEquals(-1, resultMaximize.getPoint()[1], 0);
        Assert.assertEquals(3, resultMaximize.getValue(), 0);
    }

}