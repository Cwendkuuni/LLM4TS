import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;

public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;
    private static final double RELATIVE_THRESHOLD = 1e-6;
    private static final double ABSOLUTE_THRESHOLD = 1e-8;

    @Test
    public void testDoOptimize() {
        // Test when the function attains its minimum at x = 0
        UnivariatePointValuePair expectedResult = new UnivariatePointValuePair(0, 0);
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        optimizer.setMin(-1);
        optimizer.setMax(1);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when the function attains its maximum at x = 0
        expectedResult = new UnivariatePointValuePair(0, 0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when the function is strictly increasing
        expectedResult = new UnivariatePointValuePair(1, 1);
        optimizer.setStartValue(2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when the function is strictly decreasing
        expectedResult = new UnivariatePointValuePair(-1, -1);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when the function is constant
        expectedResult = new UnivariatePointValuePair(0, 0);
        optimizer.setMin(-1);
        optimizer.setMax(1);
        optimizer.setStartValue(1);
        optimizer.setGoalType(GoalType.MINIMIZE);
        result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when the minimum is at an endpoint
        expectedResult = new UnivariatePointValuePair(-1, -1);
        optimizer.setMin(-1);
        optimizer.setMax(1);
        optimizer.setStartValue(2);
        optimizer.setGoalType(GoalType.MINIMIZE);
        result = optimizer.optimize();
        assertEquals(expectedResult, result);

        // Test when rel is less than 2 macheps
        try {
            optimizer = new BrentOptimizer(1e-20, ABSOLUTE_THRESHOLD);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Exception is expected
        }

        // Test when abs is less than or equal to 0
        try {
            optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, -1e-20);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception is expected
        }
    }

    @Test
    public void testBest() {
        // Test when a and b are null
        UnivariatePointValuePair result = BrentOptimizer.best(null, null, true);
        assertNull(result);

        // Test when a and b have different values and false isMinim
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0, 2);
        result = BrentOptimizer.best(a, b, false);
        assertEquals(b, result);

        // Test when a and b have the same value and true isMinim
        a = new UnivariatePointValuePair(0, 1);
        b = new UnivariatePointValuePair(0, 1);
        result = BrentOptimizer.best(a, b, true);
        assertEquals(a, result);
    }

    @Test
    public void testComputeObjectiveValue() {
        // Test when x = 0
        BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD);
        optimizer.setMin(-1);
        optimizer.setMax(1);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double expectedValue = 0;
        double computedValue = optimizer.computeObjectiveValue(0);
        assertEquals(expectedValue, computedValue, EPSILON);

        // Test when x = 1
        expectedValue = 1;
        computedValue = optimizer.computeObjectiveValue(1);
        assertEquals(expectedValue, computedValue, EPSILON);

        // Test when x = -1
        expectedValue = -1;
        computedValue = optimizer.computeObjectiveValue(-1);
        assertEquals(expectedValue, computedValue, EPSILON);

        // Test when the function is constant
        expectedValue = 0;
        computedValue = optimizer.computeObjectiveValue(100);
        assertEquals(expectedValue, computedValue, EPSILON);
    }

    @Test
    public void testConstructor() {
        // Test when rel < 2 macheps
        try {
            BrentOptimizer optimizer = new BrentOptimizer(1e-20, ABSOLUTE_THRESHOLD);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Exception is expected
        }

        // Test when abs <= 0
        try {
            BrentOptimizer optimizer = new BrentOptimizer(RELATIVE_THRESHOLD, -1e-20);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Exception is expected
        }
    }
}