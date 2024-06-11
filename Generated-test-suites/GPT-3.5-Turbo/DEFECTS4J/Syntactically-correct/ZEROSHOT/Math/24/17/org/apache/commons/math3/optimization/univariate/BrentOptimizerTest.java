
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0, result.getPoint(), 1e-10);
        assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(10, result.getPoint(), 1e-10);
        assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testConstructor_relTooSmall() {
        assertThrows(NumberIsTooSmallException.class, () -> new BrentOptimizer(1e-15, 1e-10));
    }

    @Test
    public void testConstructor_absLessThanOrEqualZero() {
        assertThrows(NotStrictlyPositiveException.class, () -> new BrentOptimizer(1e-10, 0));
    }
}
