
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbs() {
        optimizer = new BrentOptimizer(1.0, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidRel() {
        optimizer = new BrentOptimizer(0.0, 1.0);
    }

    @Test
    public void testDoOptimize() {
        // Test case for successful optimization
        optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(0, 1, new ObjectiveFunction() {
            public double value(double x) {
                return x*x;
            }
        }, GoalType.MINIMIZE);
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test case for optimization with custom convergence checker
        optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 1e-3;
            }
        });
        result = optimizer.optimize(0, 1, new ObjectiveFunction() {
            public double value(double x) {
                return x*x;
            }
        }, GoalType.MINIMIZE);
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);

        // Test case for unsuccessful optimization with custom convergence checker
        optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 1e-3;
            }
        });
        result = optimizer.optimize(0, 1, new ObjectiveFunction() {
            public double value(double x) {
                return -x*x;
            }
        }, GoalType.MINIMIZE);
        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }
}
