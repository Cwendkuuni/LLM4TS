import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    

    @Test
    public void testConstructorWithRelativeAndAbsoluteThresholdAndChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdLessThanMachineEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer(0.5 * Math.ulp(1.0), 0.01, null);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimizeWithSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new SimpleFunction();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = 1.5;
        double expectedValue = 1.75;
        assertEquals(expectedX, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithFunctionWithMinAtZero() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new FunctionWithMinAtZero();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = 0.0;
        double expectedValue = 1.0;
        assertEquals(expectedX, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithFunctionWithMinAtNegativeInfinity() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new FunctionWithMinAtNegativeInfinity();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = Double.NEGATIVE_INFINITY;
        double expectedValue = Double.POSITIVE_INFINITY;
        assertEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedValue, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithFunctionWithMinAtPositiveInfinity() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new FunctionWithMinAtPositiveInfinity();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = Double.POSITIVE_INFINITY;
        double expectedValue = Double.POSITIVE_INFINITY;
        assertEquals(expectedX, result.getPoint(), 0.0);
        assertEquals(expectedValue, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithFunctionWithNonMonotonicMin() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new FunctionWithNonMonotonicMin();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = 2.0;
        double expectedValue = 4.0;
        assertEquals(expectedX, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    @Test
    public void testDoOptimizeWithFunctionWithSingularities() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.01, null);
        UnivariateFunction function = new FunctionWithSingularities();
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expectedX = 1.0;
        double expectedValue = 2.0;
        assertEquals(expectedX, result.getPoint(), 0.001);
        assertEquals(expectedValue, result.getValue(), 0.001);
    }

    
}