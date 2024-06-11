import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.LinearFunction;
import org.apache.commons.math.optimization.GoalType;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    // Test cases for the optimize(UnivariateRealFunction, GoalType, double, double, double) method

    @Test
    public void testOptimize_WithValidFunctionAndValidIntervalAndValidStartValue_ReturnsOptimum() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        double result = optimizer.optimize(function, goalType, min, max, startValue);

        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testOptimize_WithValidFunctionAndValidIntervalAndMissingStartValue_ReturnsOptimum() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        double result = optimizer.optimize(function, goalType, min, max);

        assertEquals(0.0, result, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithInvalidFunction_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = null;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithInvalidGoalType_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = null;
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithMaxLessThanMin_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 10.0;
        double max = 0.0;
        double startValue = 5.0;

        optimizer.optimize(function, goalType, min, max, startValue);
    }

    // Test cases for the optimize(UnivariateRealFunction, GoalType, double, double) method

    @Test
    public void testOptimize_WithValidFunctionAndValidInterval_ReturnsOptimum() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        double result = optimizer.optimize(function, goalType, min, max);

        assertEquals(0.0, result, 1e-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithInvalidFunction_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = null;
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        optimizer.optimize(function, goalType, min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithInvalidGoalType_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = null;
        double min = 0.0;
        double max = 10.0;

        optimizer.optimize(function, goalType, min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_WithMaxLessThanMin_ThrowsIllegalArgumentException() {
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double min = 10.0;
        double max = 0.0;

        optimizer.optimize(function, goalType, min, max);
    }

    // Test cases for the localMin(boolean, UnivariateRealFunction, GoalType, double, double, double, double, double) method

    @Test
    public void testLocalMin_WithValidFunctionAndValidInterval_ReturnsOptimum() {
        boolean isMinim = true;
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 5.0;
        double hi = 10.0;
        double eps = 1e-10;
        double t = 1e-10;

        double result = optimizer.localMin(isMinim, function, goalType, lo, mid, hi, eps, t);

        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testLocalMin_WithValidFunctionAndValidIntervalAndMaxIterationsReached_ThrowsMaxIterationsExceededException() {
        boolean isMinim = true;
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 5.0;
        double hi = 10.0;
        double eps = 1e-10;
        double t = 1e-10;
        optimizer.setMaximalIterationCount(1);

        try {
            optimizer.localMin(isMinim, function, goalType, lo, mid, hi, eps, t);
            fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            // Pass
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_WithInvalidEps_ThrowsNotStrictlyPositiveException() {
        boolean isMinim = true;
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 5.0;
        double hi = 10.0;
        double eps = -1e-10;
        double t = 1e-10;

        optimizer.localMin(isMinim, function, goalType, lo, mid, hi, eps, t);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_WithInvalidT_ThrowsNotStrictlyPositiveException() {
        boolean isMinim = true;
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 5.0;
        double hi = 10.0;
        double eps = 1e-10;
        double t = -1e-10;

        optimizer.localMin(isMinim, function, goalType, lo, mid, hi, eps, t);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocalMin_WithMaxLessThanMin_ThrowsIllegalArgumentException() {
        boolean isMinim = true;
        UnivariateRealFunction function = new LinearFunction();
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 10.0;
        double mid = 5.0;
        double hi = 0.0;
        double eps = 1e-10;
        double t = 1e-10;

        optimizer.localMin(isMinim, function, goalType, lo, mid, hi, eps, t);
    }

    // Other test cases

    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize_ThrowsUnsupportedOperationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        optimizer.doOptimize();
    }

}