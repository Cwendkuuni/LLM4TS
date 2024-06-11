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
        AbstractSimplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        Assert.assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
        Assert.assertNotNull(result);
    }

    @Test
    public void testOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new AbstractSimplex();
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, optData);
        Assert.assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setStartPoint(new double[] {0.0, 0.0});
        optimizer.doOptimize();
    }

}