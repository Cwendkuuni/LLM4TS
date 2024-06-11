import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerConstructorWithException() {
        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.01);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        try {
            BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    // Additional tests for edge cases and error scenarios can be added here

}