import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new DummyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeValue() {
        new BrentOptimizer(0.5, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteValue() {
        new BrentOptimizer(1.0, -0.1);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1);

        double min = 0.0;
        double max = 5.0;

        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(min);
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.optimize(100, () -> 2.0);
        assertEquals(2.0, result.getPoint(), 0.0);
    }

    // Additional test cases for edge cases and error scenarios can be added here

    private static class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}