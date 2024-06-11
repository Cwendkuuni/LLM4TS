import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new DummyConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, checker);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithoutConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        UnivariatePointValuePair best = optimizer.best(a, b, true);

        assertEquals(b, best);
    }

    private class DummyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return true;
        }
    }

}