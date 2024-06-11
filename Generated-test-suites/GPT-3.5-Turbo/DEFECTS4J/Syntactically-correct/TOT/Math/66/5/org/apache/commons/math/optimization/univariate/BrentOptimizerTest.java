import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimizeWithStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Typical use case
        double result1 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 2);
        assertEquals(0.0, result1, 1e-10);

        // Edge case
        double result2 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 5);
        assertEquals(1.0, result2, 1e-10);

        // Error scenario
        try {
            double result3 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10, 20);
            fail("Expected Exception");
        } catch (FunctionEvaluationException e) {
            assertEquals("Function evaluation error", e.getMessage());
        }

    }

    @Test
    public void testOptimizeWithoutStartValue() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Typical use case
        double result1 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
        assertEquals(0.0, result1, 1e-10);

        // Edge case
        double result2 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
        assertEquals(1.0, result2, 1e-10);

        // Error scenario
        try {
            double result3 = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);
            fail("Expected Exception");
        } catch (MaxIterationsExceededException e) {
            assertEquals("Maximum iterations exceeded", e.getMessage());
        }

    }

    @Test
    public void testLocalMin() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Typical use case
        double result1 = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1e-10, 1e-15);
        assertEquals(0.0, result1, 1e-10);

        // Edge case
        double result2 = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1e-10, 1e-15);
        assertEquals(1.0, result2, 1e-10);

        // Error scenario
        try {
            double result3 = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, 1e-10, -1e-15);
            fail("Expected Exception");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Not strictly positive value: -1.0e-15", e.getMessage());
        }
        
        try {
            double result4 = optimizer.localMin(true, f, GoalType.MINIMIZE, 0, 5, 10, -1e-10, 1e-15);
            fail("Expected Exception");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("Not strictly positive value: -1.0e-10", e.getMessage());
        }

    }

    private UnivariateRealFunction f = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.pow(x - 1, 2);
        }
    };

}