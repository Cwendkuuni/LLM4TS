
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorWithInvalidRel() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorWithInvalidAbs() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.2);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(b, best);
    }
}
