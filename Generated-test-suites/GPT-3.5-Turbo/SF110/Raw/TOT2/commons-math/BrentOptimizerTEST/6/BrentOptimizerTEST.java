import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testConstructorWithConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, new MyConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(1.0, result.getPoint(), 1e-6);
    }

    @Test
    public void testBestMinimize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }

    @Test
    public void testBestMaximize() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        assertEquals(b, result);
    }

    class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}