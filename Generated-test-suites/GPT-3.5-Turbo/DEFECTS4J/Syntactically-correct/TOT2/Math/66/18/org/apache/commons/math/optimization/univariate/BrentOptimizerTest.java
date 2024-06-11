import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        // Test optimize method with start value
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(
                new UnivariateRealFunction() {
                    public double value(double x) {
                        return x * x - 4 * x + 4; // Sample function x^2 - 4x + 4
                    }
                },
                GoalType.MINIMIZE, 0, 2, 1);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testOptimizeWithoutStartValue() {
        // Test optimize method without start value
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(
                new UnivariateRealFunction() {
                    public double value(double x) {
                        return x * x - 4 * x + 4; // Sample function x^2 - 4x + 4
                    }
                },
                GoalType.MINIMIZE, 0, 2);
        assertEquals(2.0, result, 1e-10);
    }

    @Test
    public void testLocalMin() {
        // Test localMin method
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            double result = optimizer.localMin(false, new UnivariateRealFunction() {
                public double value(double x) {
                    return Math.sin(x); // Sample function sin(x)
                }
            }, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-10, 1e-6);
            assertEquals(Math.PI / 2, result, 1e-10);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown during local min test");
        }
    }

    @Test
    public void testInvalidEpsInLocalMin() {
        // Test for exception when eps is invalid in localMin method
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.localMin(false, new UnivariateRealFunction() {
                public double value(double x) {
                    return Math.sin(x); // Sample function sin(x)
                }
            }, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 0.0, 1e-6);
            fail("NotStrictlyPositiveException not thrown for invalid eps");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testInvalidTInLocalMin() {
        // Test for exception when t is invalid in localMin method
        BrentOptimizer optimizer = new BrentOptimizer();
        try {
            optimizer.localMin(false, new UnivariateRealFunction() {
                public double value(double x) {
                    return Math.sin(x); // Sample function sin(x)
                }
            }, GoalType.MINIMIZE, 0.0, 1.0, 2.0, 1e-10, 0.0);
            fail("NotStrictlyPositiveException not thrown for invalid t");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }
}