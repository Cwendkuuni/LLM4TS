import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create a PowellOptimizer instance
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        // Set up the optimization problem
        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {1.0, 2.0, 3.0};
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                return x * x + y * y + z * z;
            }
        };
        ConvergenceChecker<PointValuePair> checker = null;
        optimizer.setGoalType(goal);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(checker);
        optimizer.setObjectiveFunction(function);
        
        // Call the optimization method and retrieve the result
        PointValuePair result = optimizer.optimize();
        
        // Verify the result
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double expectedValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testNewPointAndDirection() {
        // Create a PowellOptimizer instance
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        // Call the newPointAndDirection() method
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        double optimum = 0.5;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        
        // Verify the result
        double[] expectedPoint = {1.05, 2.1, 3.15};
        double[] expectedDirection = {0.05, 0.1, 0.15};
        Assert.assertArrayEquals(expectedPoint, result[0], 1e-6);
        Assert.assertArrayEquals(expectedDirection, result[1], 1e-6);
    }

    @Test
    public void testSearch() {
        // Create a PowellOptimizer instance
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        
        // Call the search() method
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        UnivariatePointValuePair result = optimizer.search(p, d);
        
        // Verify the result
        double expectedPoint = 0.0;
        double expectedValue = 14.5;
        Assert.assertEquals(expectedPoint, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testLineSearch() {
        // Create a PowellOptimizer instance
        PowellOptimizer.LineSearch lineSearch = new PowellOptimizer(1e-6, 1e-6).new LineSearch(1e-6, 1e-6);
        
        // Set up test parameters
        double[] x = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        UnivariatePointValuePair optimum = new UnivariatePointValuePair(0.0, 14.5);
        
        // Call the search() method
        UnivariatePointValuePair result = lineSearch.search(x, d);
        
        // Verify the result
        Assert.assertEquals(optimum, result);
    }
}