import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BrentOptimizerTest {

    private static final double RELATIVE_THRESHOLD = 0.0001;
    private static final double ABSOLUTE_THRESHOLD = 0.01;

    private BrentOptimizer brentOptimizer;

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relativeThresholdTooSmall() {
        brentOptimizer = new BrentOptimizer(-0.0001, ABSOLUTE_THRESHOLD);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absoluteThresholdNotPositive() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, 0.0);
    }

    @Test
    public void testDoOptimize_minimizeGoalType() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        brentOptimizer.setMin(0.0);
        brentOptimizer.setStartValue(1.0);
        brentOptimizer.setMax(2.0);
        UnivariatePointValuePair result = brentOptimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), Precision.EPSILON);
        assertEquals(-0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize_maximizeGoalType() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        brentOptimizer.setMin(0.0);
        brentOptimizer.setStartValue(1.0);
        brentOptimizer.setMax(2.0);
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = brentOptimizer.doOptimize();
        assertEquals(2.0, result.getPoint(), Precision.EPSILON);
        assertEquals(-0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testBest_withNullValues() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        UnivariatePointValuePair valueA = null;
        UnivariatePointValuePair valueB = new UnivariatePointValuePair(1.0, -1.0);

        UnivariatePointValuePair bestValue = brentOptimizer.best(valueA, valueB, true);

        assertEquals(valueB, bestValue);
    }

    @Test
    public void testBest_withEqualValues() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        UnivariatePointValuePair valueA = new UnivariatePointValuePair(1.0, -1.0);
        UnivariatePointValuePair valueB = new UnivariatePointValuePair(2.0, -1.0);

        UnivariatePointValuePair bestValue = brentOptimizer.best(valueA, valueB, true);

        assertEquals(valueA, bestValue);
    }

    @Test
    public void testBest_withDifferentValues() {
        brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        UnivariatePointValuePair valueA = new UnivariatePointValuePair(1.0, -2.0);
        UnivariatePointValuePair valueB = new UnivariatePointValuePair(2.0, -1.0);

        UnivariatePointValuePair bestValue = brentOptimizer.best(valueA, valueB, true);

        assertEquals(valueB, bestValue);
    }

    @Test
    public void testBest_withMinimizeGoalType() {
        brentOptimizer = new BernoulliDistribution(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        brentOptimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair valueA = new UnivariatePointValuePair(1.0, -2.0);
        UnivariatePointValuePair valueB = new UnivariatePointValuePair(2.0, -1.0);

        UnivariatePointValuePair bestValue = brentOptimizer.best(valueA, valueB, true);

        assertEquals(valueB, bestValue);
    }

    @Test
    public void testBest_withMaximizeGoalType() {
        brentOptimizer = new BernoulliDistribution(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        brentOptimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair valueA = new UnivariatePointValuePair(1.0, -2.0);
        UnivariatePointValuePair valueB = new UnivariatePointValuePair(2.0, -1.0);

        UnivariatePointValuePair bestValue = brentOptimizer.best(valueA, valueB, true);

        assertEquals(valueA, bestValue);
    }

    @Test
    public void testComputeObjectiveValue() {
        brentOptimizer = new BernoulliDistribution(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        double result = brentOptimizer.computeObjectiveValue(2.0);

        assertEquals(-2.0, result, Precision.EPSILON);
    }

}