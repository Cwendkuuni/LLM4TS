import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithValidArguments() {
        double rel = 0.001;
        double abs = 0.01;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertTrue(optimizer.getConvergenceChecker() == null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = -0.001;
        double abs = 0.01;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.001;
        double abs = -0.01;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testDoOptimizeWithValidParameters() {
        double rel = 0.001;
        double abs = 0.01;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setConvergenceChecker(null);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 0.01); // Check if the point is close to the actual optimum
    }

    // Additional test cases can be added to cover different scenarios and edge cases.

}