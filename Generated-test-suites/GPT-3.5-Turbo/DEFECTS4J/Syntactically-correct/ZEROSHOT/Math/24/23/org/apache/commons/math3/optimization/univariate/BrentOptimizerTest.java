import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 1e-3;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        GoalType goal = GoalType.MINIMIZE;

        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(goal);

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelTooSmall() {
        double rel = 1e-10;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsNegative() {
        double rel = 1e-3;
        double abs = -1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testConstructorValidArguments() {
        double rel = 1e-3;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        assertNotNull(optimizer);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6, null);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertEquals(b, result);
    }

    @Test
    public void testBestBothNull() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;

        BrentOptimizer optimizer = new BrentOptimizer(1e-3, 1e-6, null);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        assertNull(result);
    }

}