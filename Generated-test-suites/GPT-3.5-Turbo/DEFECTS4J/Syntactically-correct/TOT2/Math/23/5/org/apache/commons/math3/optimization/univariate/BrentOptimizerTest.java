import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return true; // Always return true for testing purposes
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, checker);

        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);

        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(0.0);

        UnivariatePointValuePair result = optimizer.optimize(100, null);

        assertNotNull(result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeRelativeTolerance() {
        new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroAbsoluteTolerance() {
        new BrentOptimizer(0.001, 0.0);
    }
}