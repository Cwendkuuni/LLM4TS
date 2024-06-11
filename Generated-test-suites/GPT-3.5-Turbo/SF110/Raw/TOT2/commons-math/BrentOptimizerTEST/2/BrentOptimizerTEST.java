import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration > 5;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        // Test the optimization process
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        // Test the best method with valid inputs
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = new BrentOptimizer(0.01, 0.1).best(a, b, true);
        assertEquals(b, result);
    }

    @Test
    public void testBestWithNullInputs() {
        // Test the best method with null inputs
        UnivariatePointValuePair result = new BrentOptimizer(0.01, 0.1).best(null, null, true);
        assertNull(result);
    }

    @Test
    public void testBestWithOppositeGoalType() {
        // Test the best method with opposite goal type
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 5.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 10.0);
        UnivariatePointValuePair result = new BrentOptimizer(0.01, 0.1).best(a, b, false);
        assertEquals(a, result);
    }
}