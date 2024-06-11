import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNull(result);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1, 0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }
}