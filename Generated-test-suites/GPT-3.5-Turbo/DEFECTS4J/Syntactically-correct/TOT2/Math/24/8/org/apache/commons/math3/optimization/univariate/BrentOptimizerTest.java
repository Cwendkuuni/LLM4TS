import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with ConvergenceChecker
        // Edge case: null convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without ConvergenceChecker
        // Edge case: empty parameter
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test doOptimize()
        // Typical case: valid input
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        // Test best()
        // Typical case: valid inputs
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;
        UnivariatePointValuePair bestResult = optimizer.best(a, b, isMinim);
        assertEquals(bestResult, b);
        // Edge case: null inputs
        a = null;
        b = null;
        bestResult = optimizer.best(a, b, isMinim);
        assertNull(bestResult);
    }

}