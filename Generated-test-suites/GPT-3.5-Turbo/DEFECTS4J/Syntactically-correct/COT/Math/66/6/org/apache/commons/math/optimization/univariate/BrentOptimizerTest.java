import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, GoalType.MINIMIZE, 0, 2 * Math.PI);
        assertEquals(0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, GoalType.MINIMIZE, 0, 2 * Math.PI, Math.PI / 2);
        assertEquals(Math.PI / 2, result, 1e-10);
    }

}