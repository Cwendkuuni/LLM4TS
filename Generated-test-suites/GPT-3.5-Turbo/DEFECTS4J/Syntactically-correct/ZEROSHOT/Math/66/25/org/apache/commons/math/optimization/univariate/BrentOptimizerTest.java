import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        double startValue = 5;

        try {
            double result = optimizer.optimize(f, goalType, min, max, startValue);
            assertEquals(result, 0.0, 1E-10);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        try {
            double result = optimizer.optimize(f, goalType, min, max);
            assertEquals(result, 0.0, 1E-10);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };

        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10;
        double mid = 0;
        double hi = 10;
        double eps = 1E-10;
        double t = 1E-14;

        try {
            double result = optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
            assertEquals(result, 0.0, 1E-10);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}