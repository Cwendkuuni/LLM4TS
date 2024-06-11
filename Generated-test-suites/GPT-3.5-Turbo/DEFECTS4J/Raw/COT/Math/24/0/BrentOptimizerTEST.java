import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        double rel = 1e-6;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        // Test case 1
        double min = 0;
        double max = 10;
        UnivariatePointValuePair result = optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, min, max);
        double expectedMinValue = 0;
        double expectedMinPoint = 0;
        assertEquals(expectedMinValue, result.getValue(), 1e-6);
        assertEquals(expectedMinPoint, result.getPoint(), 1e-6);

        // Test case 2
        min = -50;
        max = 50;
        result = optimizer.optimize(1000, new UnivariateObjectiveFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, GoalType.MAXIMIZE, min, max);
        double expectedMaxValue = 1;
        double expectedMaxPoint = Math.PI / 2;
        assertEquals(expectedMaxValue, result.getValue(), 1e-6);
        assertEquals(expectedMaxPoint, result.getPoint(), 1e-6);
        
        // Additional test cases for edge cases and exceptions
        // ...

    }
}