import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class PowellOptimizerTest {

    // Initialize PowellOptimizer object for testing
    private static final PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.0001);

    @Test
    public void testDoOptimize() {
        // Test the doOptimize method
        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {0, 1, 2};
        PointValuePair expected = new PointValuePair(startPoint, 0);
        PointValuePair result = optimizer.optimize(100, startPoint, goal, new ConvergenceChecker<>());
        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerNotStrictlyPositiveException() {
        // Test PowellOptimizer initializes with negative absolute threshold
        new PowellOptimizer(0.0001, -0.0001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerNumberIsTooSmallException() {
        // Test PowellOptimizer initializes with relative threshold less than minimum
        new PowellOptimizer(0.00001, 0.0001);
    }

    @Test
    public void testSearch() {
        // Initialize LineSearch object for testing
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(0.0001, 0.0001);

        // Test search method
        double[] startingPoint = {0, 0};
        double[] searchDirection = {1, 0};
        PowellOptimizer.LineSearch.UnivariatePointValuePair result = lineSearch.search(startingPoint, searchDirection);
        Assert.assertEquals(0, result.getPoint(), 0.0001);
        Assert.assertEquals(0, result.getValue(), 0.0001);
    }

    @Test
    public void testNewPointAndDirection() {
        // Test newPointAndDirection method
        double[] startingPoint = {0, 0};
        double[] searchDirection = {1, 1};
        double stepSize = 0.5;
        double[][] expected = {{0.5, 0.5}, {0.5, 0.5}};
        double[][] result = optimizer.newPointAndDirection(startingPoint, searchDirection, stepSize);
        Assert.assertArrayEquals(expected[0], result[0], 0.0001);
        Assert.assertArrayEquals(expected[1], result[1], 0.0001);
    }

}