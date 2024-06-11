import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        double result = optimizer.doOptimize().getValue();
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, -1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, -2);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        assertEquals(a, result);
    }
}