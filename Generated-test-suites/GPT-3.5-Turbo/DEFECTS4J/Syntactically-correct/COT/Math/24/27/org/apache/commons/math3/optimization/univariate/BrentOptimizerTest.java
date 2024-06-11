import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer brentOptimizer1 = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result1 = brentOptimizer1.doOptimize();
        assertNotNull(result1);

        // Test case 2
        BrentOptimizer brentOptimizer2 = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result2 = brentOptimizer2.doOptimize();
        assertNotNull(result2);
    }

    @Test
    public void testBest() {
        // Test case 1
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair result1 = brentOptimizer.best(a, b, true);
        assertEquals(result1, b);

        // Test case 2
        UnivariatePointValuePair c = new UnivariatePointValuePair(2.0, 0.2);
        UnivariatePointValuePair d = new UnivariatePointValuePair(3.0, 0.3);
        UnivariatePointValuePair result2 = brentOptimizer.best(c, d, false);
        assertEquals(result2, c);
    }

    @Test
    public void testConstructor() {
        // Test case 1
        BrentOptimizer brentOptimizer1 = new BrentOptimizer(0.1, 0.01);
        assertNotNull(brentOptimizer1);

        // Test case 2
        BrentOptimizer brentOptimizer2 = new BrentOptimizer(0.01, 0.001);
        assertNotNull(brentOptimizer2);
    }
}