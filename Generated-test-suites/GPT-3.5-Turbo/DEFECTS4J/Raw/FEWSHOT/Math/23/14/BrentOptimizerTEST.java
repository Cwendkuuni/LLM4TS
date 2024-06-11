import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveAbsoluteThreshold() {
        new BrentOptimizer(1, -1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdBelowMinimum() {
        new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE / 2, 1);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(100, new MyFunction(), GoalType.MINIMIZE, 1, 10);
        assertEquals(1.0, result.getPoint(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    private class MyFunction implements UnivariateFunction {
        public double value(double x) {
            return x * x;
        }
    }
}