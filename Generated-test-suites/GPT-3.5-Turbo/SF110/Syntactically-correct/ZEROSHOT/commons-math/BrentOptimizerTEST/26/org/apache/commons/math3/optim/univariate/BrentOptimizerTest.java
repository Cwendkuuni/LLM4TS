import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsNumberIsTooSmallException() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE - 1, 1, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsNotStrictlyPositiveException() {
        new BrentOptimizer(1, 0, null);
    }

    @Test
    public void testDoOptimize() {
        double rel = 1e-10;
        double abs = 1e-12;
        ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Test the doOptimize() method here
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 1, null);

        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        assertEquals(b, result1);

        UnivariatePointValuePair c = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair result2 = optimizer.best(b, c, true);
        assertEquals(c, result2);

        UnivariatePointValuePair d = null;
        UnivariatePointValuePair result3 = optimizer.best(a, d, true);
        assertEquals(a, result3);

        UnivariatePointValuePair e = new UnivariatePointValuePair(3, 3);
        UnivariatePointValuePair result4 = optimizer.best(d, e, false);
        assertEquals(e, result4);
    }
}