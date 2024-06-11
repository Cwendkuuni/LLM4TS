import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructor() {
        double rel = 0.01;
        double abs = 0.001;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerConstructorWithInvalidRel() {
        double rel = 0.0;
        double abs = 0.001;
        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            fail("Exception not thrown");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testBrentOptimizerConstructorWithInvalidAbs() {
        double rel = 0.01;
        double abs = -0.001;
        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            fail("Exception not thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x - 4, GoalType.MINIMIZE, 0, 5);
        assertEquals(2.0, result.getPoint(), 1e-6);
        assertEquals( 0.0, result.getValue(), 1e-6);
    }
}