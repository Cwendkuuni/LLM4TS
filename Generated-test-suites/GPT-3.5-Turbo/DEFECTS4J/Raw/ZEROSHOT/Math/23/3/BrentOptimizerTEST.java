import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testDoOptimize_maximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    @Test
    public void testDoOptimize_customConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setConvergenceChecker(new MyConvergenceChecker());

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
    }

    private class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);

        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);

        result = optimizer.best(a, b, false);
        assertEquals(b, result);
    }
}