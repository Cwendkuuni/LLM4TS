import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = null;
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        double[] startPoint = {0, 0, 0};
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }
}