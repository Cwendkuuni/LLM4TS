import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Test constructor with ConvergenceChecker
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Test constructor without ConvergenceChecker
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test the doOptimize method
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        // Test the best method with different UnivariatePointValuePair instances
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(result, a);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRelativeTolerance() {
        // Test constructor with small relative tolerance
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveAbsoluteThreshold() {
        // Test constructor with non-positive absolute threshold
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
    }
}