import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

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
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new InitialGuess(new double[]{1.0, 1.0});
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = new InitialGuess(new double[]{1.0, 1.0});
        PointValuePair result = optimizer.doOptimize(maxEval, f, goalType, optData);
        double[] expectedPoint = {0.0, 0.0};
        double expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        Assert.assertEquals(expectedValue, result.getValue(), 0.0);
    }
}