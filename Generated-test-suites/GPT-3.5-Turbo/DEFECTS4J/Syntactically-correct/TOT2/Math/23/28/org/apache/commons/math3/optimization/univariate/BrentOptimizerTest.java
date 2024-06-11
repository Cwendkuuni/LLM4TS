import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithNegativeValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(-2.0);
        optimizer.setStartValue(-1.0);
        optimizer.setMax(0.0);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(-1.0, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
    }

    // Additional tests for edge cases and error scenarios can be added here

}