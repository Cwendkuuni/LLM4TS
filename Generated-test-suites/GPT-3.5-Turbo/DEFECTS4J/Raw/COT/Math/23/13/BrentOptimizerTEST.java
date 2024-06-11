import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Helper method to create a BrentOptimizer instance
    private BrentOptimizer createBrentOptimizer(double rel, double abs, ConvergenceChecker<UnivariatePointValuePair> checker) {
        try {
            return new BrentOptimizer(rel, abs, checker);
        } catch (NotStrictlyPositiveException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        return null;
    }

    // Helper method to create a BrentOptimizer instance without a convergence checker
    private BrentOptimizer createBrentOptimizer(double rel, double abs) {
        return createBrentOptimizer(rel, abs, null);
    }

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = createBrentOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRel() {
        createBrentOptimizer(-0.01, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbs() {
        createBrentOptimizer(0.01, -0.001);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = createBrentOptimizer(0.01, 0.001);

        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);

        UnivariatePointValuePair result = optimizer.optimize(100, GoalType.MINIMIZE, null);
        assertNotNull(result);
    }
}