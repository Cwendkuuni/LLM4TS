import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    // Test optimizeInternal method
    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        OptimizationData[] optData = {};

        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);

        Assert.assertNotNull(result);
    }

    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new AbstractSimplex() {
        });

        PointValuePair result = optimizer.doOptimize();

        Assert.assertNotNull(result);
    }
}