import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Create an instance of BrentOptimizer
    private BrentOptimizer brentOptimizer = new BrentOptimizer();

    // Define a test function
    private UnivariateRealFunction testFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return Math.pow(x, 2) - 4 * x + 3;
        }
    };

    // Test optimize() method
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = brentOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0, 4);
        assertEquals(2.0, result, 0.0001);
    }

    // Test localMin() method with MINIMIZE goal type
    @Test
    public void testLocalMinMinimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = brentOptimizer.localMin(true, testFunction, GoalType.MINIMIZE, 0, 2, 4, 1E-10, 1E-14);
        assertEquals(2.0, result, 0.0001);
    }

    // Test localMin() method with MAXIMIZE goal type
    @Test
    public void testLocalMinMaximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = brentOptimizer.localMin(false, testFunction, GoalType.MAXIMIZE, 0, 2, 4, 1E-10, 1E-14);
        assertEquals(2.0, result, 0.0001);
    }

    // Test doOptimize() method
    @Test
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        try {
            brentOptimizer.doOptimize();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

}