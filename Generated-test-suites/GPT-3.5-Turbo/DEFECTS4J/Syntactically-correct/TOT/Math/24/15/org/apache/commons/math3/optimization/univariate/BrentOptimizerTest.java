import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    private static final double DEFAULT_RELATIVE_THRESHOLD = 2 * Math.ulp(1d);
    private static final double DEFAULT_ABSOLUTE_THRESHOLD = 1E-5;

    @Test
    public void testDoOptimize_minimize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_THRESHOLD, DEFAULT_ABSOLUTE_THRESHOLD, checker);

        double minValue = optimizer.doOptimize().getPoint();
        // Expected minimum value
        double expectedMinValue = 0.0;

        // Assert
        Assert.assertEquals(expectedMinValue, minValue, DEFAULT_ABSOLUTE_THRESHOLD);
    }

    @Test
    public void testDoOptimize_maximize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(DEFAULT_RELATIVE_THRESHOLD, DEFAULT_ABSOLUTE_THRESHOLD, checker);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        double maxValue = optimizer.doOptimize().getPoint();
        // Expected maximum value
        double expectedMaxValue = 10.0;

        // Assert
        Assert.assertEquals(expectedMaxValue, maxValue, DEFAULT_ABSOLUTE_THRESHOLD);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relTooSmall() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relThreshold = 1E-16;
        double absThreshold = DEFAULT_ABSOLUTE_THRESHOLD;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absNonPositive() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relThreshold = DEFAULT_RELATIVE_THRESHOLD;
        double absThreshold = -1E-5;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, checker);
    }

}