import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-6;

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
    }

    // Test cases for the doOptimize() method
    @Test
    public void testDoOptimize_Minimize() {
        double min = -10;
        double max = 10;
        double start = 0;
        double expected = -5;

        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(start);

        UnivariatePointValuePair result = optimizer.optimize();
        double actual = result.getPoint();

        Assert.assertEquals(expected, actual, EPSILON);
    }

    @Test
    public void testDoOptimize_Maximize() {
        double min = -10;
        double max = 10;
        double start = 0;
        double expected = 5;

        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(start);

        UnivariatePointValuePair result = optimizer.optimize();
        double actual = result.getPoint();

        Assert.assertEquals(expected, actual, EPSILON);
    }

    // Test cases for the default constructor
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_Default_RelativeThresholdTooSmall() {
        new BrentOptimizer(1e-7, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_Default_AbsoluteThresholdNotStrictlyPositive() {
        new BrentOptimizer(1e-6, 0);
    }

    // Test cases for the custom constructor
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_Custom_RelativeThresholdTooSmall() {
        new BrentOptimizer(1e-7, 1e-6, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_Custom_AbsoluteThresholdNotStrictlyPositive() {
        new BrentOptimizer(1e-6, 0, null);
    }

    // Test cases for the best() method
    @Test
    public void testBest_Minimize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);

        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(b, result);
    }

    @Test
    public void testBest_Maximize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 5);

        UnivariatePointValuePair result = optimizer.best(a, b, false);

        Assert.assertEquals(a, result);
    }

}