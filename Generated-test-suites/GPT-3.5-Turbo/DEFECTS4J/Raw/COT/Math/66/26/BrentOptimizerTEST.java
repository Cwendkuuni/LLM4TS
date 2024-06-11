import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        // Test optimize method
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test with default parameters
        double result1 = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -10, 10);
        // Assert the result
        assert Math.abs(result1) < 1e-6;

        // Test with specified start value
        double result2 = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, -10, 10, 5);
        // Assert the result
        assert Math.abs(result2) < 1e-6;
    }

    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test localMin method
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test with interval (0, 10), goal type MINIMIZE
        double result1 = optimizer.localMin(true, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0, 5, 10, 1e-6, 1e-6);
        // Assert the result
        assert Math.abs(result1) < 1e-6;

        // Test with interval (0, 10), goal type MAXIMIZE
        double result2 = optimizer.localMin(false, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MAXIMIZE, 0, 5, 10, 1e-6, 1e-6);
        // Assert the result
        assert Math.abs(result2) < 1e-6;

        // Test with interval (0, 10), goal type MINIMIZE, exception handling
        boolean exceptionThrown = false;
        try {
            optimizer.localMin(true, new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x;
                }
            }, GoalType.MINIMIZE, 0, 5, 10, 0, 1e-6);
        } catch (NotStrictlyPositiveException e) {
            exceptionThrown = true;
        }
        // Assert the exception
        assert exceptionThrown;
    }
}