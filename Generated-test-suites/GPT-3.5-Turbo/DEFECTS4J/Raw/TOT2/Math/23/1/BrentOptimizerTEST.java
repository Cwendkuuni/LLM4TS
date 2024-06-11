import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-10, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerConstructorWithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-10);
        assertNotNull(optimizer);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-10);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 8.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(a, best);
    }

    // Add more test cases for other methods in BrentOptimizer class

}