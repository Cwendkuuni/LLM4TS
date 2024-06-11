import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        // Typical case
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutChecker() {
        // Edge case
        double rel = 0.0;
        double abs = 1e-6;
        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize() {
        // Typical case
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        assertNotNull(optimizer.doOptimize());
    }

    @Test
    public void testBest() {
        // Edge case
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(b, result);
    }

}