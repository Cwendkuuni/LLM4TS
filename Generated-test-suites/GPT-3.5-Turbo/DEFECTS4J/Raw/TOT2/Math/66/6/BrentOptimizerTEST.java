import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        double startValue = 2.0;

        try {
            double result = optimizer.optimize(f, goalType, min, max, startValue);
            assertEquals(2.0, result, 1e-6);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;

        try {
            double result = optimizer.optimize(f, goalType, min, max);
            assertTrue(result >= -Math.PI / 2 && result <= Math.PI / 2);
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testLocalMinWithInvalidTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = -1.0;
        double t = 0.5;

        try {
            optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
            fail("Expected NotStrictlyPositiveException");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

}