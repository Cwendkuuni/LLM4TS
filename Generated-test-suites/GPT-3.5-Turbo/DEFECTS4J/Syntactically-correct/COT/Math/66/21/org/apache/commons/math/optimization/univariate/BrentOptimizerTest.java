import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    // Test optimize method with startValue
    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, GoalType.MINIMIZE, 1, 3, 2);
            assertEquals(2, result, 0.0001);
        } catch (Exception e) {
            fail("Unexpected Exception!");
        }
    }
    
    // Test optimize method without startValue
    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.optimize(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, GoalType.MINIMIZE, 1, 3);
            assertEquals(2, result, 0.0001);
        } catch (Exception e) {
            fail("Unexpected Exception!");
        }
    }
    
    // Test optimize method with invalid goalType
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidGoalType() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, GoalType.EITHER, 1, 3);
    }
    
    // Test localMin method
    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.localMin(true, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 4;
                }
            }, GoalType.MINIMIZE, 1, 2, 3, 0.0001, 0.0001);
            assertEquals(2, result, 0.0001);
        } catch (Exception e) {
            fail("Unexpected Exception!");
        }
    }
}