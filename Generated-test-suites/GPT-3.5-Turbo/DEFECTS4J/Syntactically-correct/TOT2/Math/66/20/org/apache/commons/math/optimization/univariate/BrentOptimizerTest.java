import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_StartValue() {
        // Test optimize method with startValue provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        }, GoalType.MINIMIZE, 0, 4, 2);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testOptimize_NoStartValue() {
        // Test optimize method without startValue provided
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        }, GoalType.MINIMIZE, 0, 4);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testLocalMin() {
        // Test localMin method with typical values
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -1, 0, 1, 1e-6, 1e-6);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_NegativeEps() {
        // Test localMin method with negative epsilon
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -1, 0, 1, -1, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_NegativeT() {
        // Test localMin method with negative tolerance
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -1, 0, 1, 1e-6, -1);
    }

    // Additional test cases for other methods can be added here

}