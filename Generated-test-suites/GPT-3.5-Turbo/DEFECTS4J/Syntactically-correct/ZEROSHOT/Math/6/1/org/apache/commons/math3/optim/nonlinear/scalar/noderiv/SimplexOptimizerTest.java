import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    @Test
    public void testConstructor() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithThresholds() {
        SimplexOptimizer optimizer = new SimplexOptimizer(0.1, 0.01);
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testSetSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
    }

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new AbstractSimplex();
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }
}