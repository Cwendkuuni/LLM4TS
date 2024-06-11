import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Create instance of BrentOptimizer
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Test the constructor
        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);

        // Test the doOptimize method
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertNotNull(result);

        // Test the best method
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertSame(a, best);
    }
}