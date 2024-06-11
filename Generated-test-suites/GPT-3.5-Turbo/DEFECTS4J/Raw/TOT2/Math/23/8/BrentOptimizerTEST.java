import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);

        double result = optimizer.optimize();

        assertEquals(0, result, 1e-6);
    }

    @Test
    public void testDoOptimizeWithNullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);

        double result = optimizer.optimize();

        assertNotEquals(5.0, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-6);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair best = optimizer.best(a, b, true);

        assertEquals(b, best);
    }
}