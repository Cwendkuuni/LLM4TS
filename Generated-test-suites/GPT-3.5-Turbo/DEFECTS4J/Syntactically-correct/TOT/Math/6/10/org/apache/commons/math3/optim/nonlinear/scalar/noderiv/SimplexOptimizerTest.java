import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimizeInternal_minimize() {
        // Typical use case
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair expected = new PointValuePair(new double[]{0, 0}, 0);

        PointValuePair result = optimizer.optimizeInternal(100, f, goalType);

        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeInternal_maximize() {
        // Typical use case
        MultivariateFunction f = point -> -1 * point[0] * point[0] - point[1] * point[1];
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair expected = new PointValuePair(new double[]{0, 0}, 0);

        PointValuePair result = optimizer.optimizeInternal(100, f, goalType);

        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeInternal_noIterations() {
        // Edge case: maxEval = 0
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = optimizer.optimizeInternal(0, f, goalType);

        Assert.assertNull(result);
    }

    @Test
    public void testOptimizeInternal_nullFunction() {
        // Error scenario: null objective function
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair expected = null;

        PointValuePair result = optimizer.optimizeInternal(100, null, goalType);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOptimizeInternal_invalidGoalType() {
        // Error scenario: invalid goal type
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = null;
        PointValuePair expected = null;

        PointValuePair result = optimizer.optimizeInternal(100, f, goalType);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDoOptimize_convergence() {
        // Typical use case: Convergence reached
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1, 0};
        PointValuePair expected = new PointValuePair(new double[]{0, 0}, 0);

        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.doOptimize();

        Assert.assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
        Assert.assertEquals(expected.getValue(), result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimize_noConvergence() {
        // Typical use case: Convergence not reached
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {1, 0};

        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.doOptimize();

        Assert.assertNotNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_nullSimplex() {
        // Error scenario: null simplex
        MultivariateFunction f = point -> point[0] * point[0] + point[1] * point[1];
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {0, 0};

        optimizer.setStartPoint(startPoint);
        optimizer.setSimplex(null);

        optimizer.doOptimize();
    }
}