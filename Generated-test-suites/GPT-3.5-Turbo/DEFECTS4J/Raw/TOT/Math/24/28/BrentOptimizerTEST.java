import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize() {
        // Test with a simple quadratic function: f(x) = x^2
        double minValue = 0.0;
        double minLocation = 0.0;
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);
        UnivariatePointValuePair result = optimizer.optimize(function, null, -10, 10);
        assertEquals(minValue, result.getValue(), 1e-6);
        assertEquals(minLocation, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(1e-20, 1e-6);
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
        assertNotNull(optimizer.getConvergenceChecker());
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-20);
        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-20);
    }
}