import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-0.001, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, -0.001);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }

}