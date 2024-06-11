import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);

        UnivariatePointValuePair result = optimizer.optimize(1000, x -> x * x - 4 * x);

        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals(-4.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithInvalidAbsThreshold() {
        try {
            new BrentOptimizer(1e-6, -1e-6);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}