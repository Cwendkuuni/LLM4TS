import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1E-6, 1E-4, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        new BrentOptimizer(1E-12, 1E-4, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        new BrentOptimizer(1E-6, 0, null);
    }

    @Test
    public void testDoOptimizeWithMinimizerGoalType() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, result.getPoint(), 1E-6);
    }

    @Test
    public void testDoOptimizeWithMaximizerGoalType() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setConvergenceChecker(null);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(10, result.getPoint(), 1E-6);
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker((iter, previous, current) -> {
            if (iter >= 10) {
                return true;
            } else {
                return false;
            }
        });

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, result.getPoint(), 1E-6);
    }

    @Test
    public void testDoOptimizeWithEdgeCase() {
        optimizer.setMin(0);
        optimizer.setMax(0);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, result.getPoint(), 1E-6);
    }

    @Test
    public void testDoOptimizeWithErrorScenario() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(15);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNull(result);
    }

}