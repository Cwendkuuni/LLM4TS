import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize_typical() {
        double result = optimizer.doOptimize();
        // add assertions here
    }

    @Test
    public void testDoOptimize_edge() {
        double result = optimizer.doOptimize();
        // add assertions here
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relTooSmall() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absInvalid() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

}