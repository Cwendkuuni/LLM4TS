import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Test case for doOptimize method
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0.0, result.getValue(), 1e-10);
    }

    @Test
    public void testBest() {
        // Test case for best method
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);

        UnivariatePointValuePair bestResult = optimizer.best(a, b, true);
        assertEquals(b, bestResult);
    }

    @Test
    public void testConstructorWithChecker() {
        // Test case for constructor with convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10, checker);
        assertTrue(optimizer.getConvergenceChecker() == checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbs() {
        // Test case for constructor with negative absolute threshold
        new BrentOptimizer(1e-10, -1e-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithLowRel() {
        // Test case for constructor with low relative threshold
        new BrentOptimizer(1e-15, 1e-10);
    }

}