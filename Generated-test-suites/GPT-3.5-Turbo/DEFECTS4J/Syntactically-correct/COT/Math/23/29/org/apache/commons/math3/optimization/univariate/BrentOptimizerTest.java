import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeThreshold() {
        double rel = 1e-6;
        double abs = -1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRelativeThreshold() {
        double rel = 1e-16;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, new CustomConvergenceChecker());
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimizeMinimize() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeMaximize() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(10, result.getPoint(), 1e-6);
        assertEquals(100, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithCustomConvergenceChecker() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, new CustomConvergenceChecker());
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        UnivariatePointValuePair result = optimizer.optimize();

        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    private class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getPoint() < 1e-3;
        }
    }
}