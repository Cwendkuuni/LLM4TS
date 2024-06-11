
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        // Test valid constructor
        double rel = 0.01;
        double abs = 0.1;
        BrentOptimizer opt = new BrentOptimizer(rel, abs);
        assertEquals(rel, opt.relativeThreshold, 0.0);
        assertEquals(abs, opt.absoluteThreshold, 0.0);
        assertNull(opt.getConvergenceChecker());

        // Test invalid constructor
        try {
            rel = 0.001;
            abs = 0.0;
            opt = new BrentOptimizer(rel, abs);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            rel = 0.001;
            abs = -0.1;
            opt = new BrentOptimizer(rel, abs);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        try {
            rel = 0.0;
            abs = 0.1;
            opt = new BrentOptimizer(rel, abs);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        // Create a test case
        double rel = 0.01;
        double abs = 0.1;
        BrentOptimizer opt = new BrentOptimizer(rel, abs);

        // Test optimization with minimum goal type
        double min = -10.0;
        double max = 10.0;
        GoalType goal = GoalType.MINIMIZE;
        double expectedMinValue = 0.0;
        double expectedMinPoint = 0.0;
        UnivariatePointValuePair result = opt.optimize(Integer.MAX_VALUE, goal, min, max);
        assertEquals(expectedMinValue, result.getValue(), 0.0);
        assertEquals(expectedMinPoint, result.getPoint(), 0.0);

        // Test optimization with maximum goal type
        goal = GoalType.MAXIMIZE;
        double expectedMaxValue = -expectedMinValue;
        double expectedMaxPoint = expectedMinPoint;
        result = opt.optimize(Integer.MAX_VALUE, goal, min, max);
        assertEquals(expectedMaxValue, result.getValue(), 0.0);
        assertEquals(expectedMaxPoint, result.getPoint(), 0.0);
    }
}
