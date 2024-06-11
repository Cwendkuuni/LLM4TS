
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testConstructor() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, checker);
        assertEquals(0.01, optimizer.relativeThreshold, Precision.EPSILON);
        assertEquals(0.1, optimizer.absoluteThreshold, Precision.EPSILON);

        try {
            optimizer = new BrentOptimizer(-0.01, 0.1, checker);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }

        try {
            optimizer = new BrentOptimizer(0.01, -0.1, checker);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, null);
        optimizer.setMin(-1.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), Precision.EPSILON);
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertSame(b, result);

        result = optimizer.best(a, b, false);
        assertSame(a, result);
    }
}
