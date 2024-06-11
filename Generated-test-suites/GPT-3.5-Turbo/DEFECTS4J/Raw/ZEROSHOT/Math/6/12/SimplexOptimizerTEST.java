import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        // Test case 1
        int maxEval = 1000;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optData = new OptimizationData[0];
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        double[] expectedPoint = new double[] { 0.0, 0.0 };
        double expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
        
        // Test case 2
        maxEval = 2000;
        f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] - point[1] * point[1];
            }
        };
        goalType = GoalType.MAXIMIZE;
        result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        expectedPoint = new double[] { 0.0, 0.0 };
        expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
        
        // Test case 3
        maxEval = 500;
        f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        };
        goalType = GoalType.MINIMIZE;
        result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
        expectedPoint = new double[] { 0.0, 0.0 };
        expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
        Assert.assertEquals(expectedValue, result.getValue(), 0.001);
    }
}