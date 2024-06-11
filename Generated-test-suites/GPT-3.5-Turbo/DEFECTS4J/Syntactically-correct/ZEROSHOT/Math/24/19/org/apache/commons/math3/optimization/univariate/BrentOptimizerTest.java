import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test case for BrentOptimizer constructor with convergence checker
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithConvergenceChecker()
            throws NotStrictlyPositiveException, NumberIsTooSmallException {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration,
                                     UnivariatePointValuePair previous,
                                     UnivariatePointValuePair current) {
                return FastMath.abs(current.getValue() - previous.getValue()) < 0.001;
            }
        };
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001, checker);
        assertNotNull(brentOptimizer);
    }

    // Test case for BrentOptimizer constructor without convergence checker
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithoutConvergenceChecker()
            throws NotStrictlyPositiveException, NumberIsTooSmallException {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        assertNotNull(brentOptimizer);
    }

    // Test case for doOptimize method
    @Test
    public void testDoOptimize() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(0.01, 0.001);
        double result = brentOptimizer.doOptimize().getPoint();
        assertEquals(0.0, result, 0.001);
    }

    // Test case for best method when a is null
    @Test
    public void testBestWithANull() {
        UnivariatePointValuePair b = new UnivariatePointValuePair(5.0, 10.0);
        UnivariatePointValuePair result = brentOptimizer.best(null, b, true);
        assertEquals(b, result);
    }

    // Test case for best method when b is null
    @Test
    public void testBestWithBNull() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2.0, 8.0);
        UnivariatePointValuePair result = brentOptimizer.best(a, null, true);
        assertEquals(a, result);
    }

    // Test case for best method when a and b are null
    @Test
    public void testBestWithBothNull() {
        UnivariatePointValuePair result = brentOptimizer.best(null, null, true);
        assertNull(result);
    }

    // Test case for best method with isMinim = true
    @Test
    public void testBestWithIsMinimTrue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2.0, 8.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(5.0, 10.0);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, true);
        assertEquals(a, result);
    }

    // Test case for best method with isMinim = false
    @Test
    public void testBestWithIsMinimFalse() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2.0, 8.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(5.0, 10.0);
        UnivariatePointValuePair result = brentOptimizer.best(a, b, false);
        assertEquals(b, result);
    }
}