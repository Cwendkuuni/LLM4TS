import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Test the BrentOptimizer constructor with convergence checker
        // Check if the relative threshold and absolute threshold are set correctly
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, null);
        assertEquals(0.01, optimizer.relativeThreshold, 0.0);
        assertEquals(0.1, optimizer.absoluteThreshold, 0.0);
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Test the BrentOptimizer constructor without convergence checker
        // Check if the default convergence checker is initialized correctly
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize() {
        // Test the doOptimize method of BrentOptimizer
        // Check if the optimization returns the expected result within tolerance
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, null);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        // Test the best method of BrentOptimizer
        // Check if the method returns the best UnivariatePointValuePair based on goal type
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(b, best);
    }
}