import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        ObjectiveFunction function = new ObjectiveFunction(x -> x * x - 4 * x);
        optimizer.optimize(function, null, null, null);
        // Add assertions here based on expected output
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 1);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(result.getValue(), 2.0, 0.0);
    }

    // Add more test cases for edge cases and error scenarios

}