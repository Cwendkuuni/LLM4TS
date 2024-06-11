import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 2.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }
}