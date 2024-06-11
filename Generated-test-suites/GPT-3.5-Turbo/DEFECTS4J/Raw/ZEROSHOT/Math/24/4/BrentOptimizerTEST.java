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
    public void testDoOptimize() {
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        double expectedResult = 1.0;

        UnivariatePointValuePair result = optimizer.optimize(100, min, max, startValue);
        assertEquals(expectedResult, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(1e-6, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(1e-7, 1e-6);
    }
}