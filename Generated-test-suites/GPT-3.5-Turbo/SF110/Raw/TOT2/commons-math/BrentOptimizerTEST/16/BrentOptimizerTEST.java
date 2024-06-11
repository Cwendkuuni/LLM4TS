import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class BrentOptimizerTest {

    // Test for constructor with ConvergenceChecker parameter
    @Test
    public void testConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001, null);
        Assert.assertNotNull(optimizer);
    }

    // Test for constructor without ConvergenceChecker parameter
    @Test
    public void testConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        Assert.assertNotNull(optimizer);
    }

    // Test for doOptimize method
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        // Add test cases for edge cases and typical use cases
        // Assert statements for the expected results
    }

    // Test for best method
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertEquals(b, result);
    }

    // Test for edge case where relative threshold is less than MIN_RELATIVE_TOLERANCE
    @Test(expected = NumberIsTooSmallException.class)
    public void testRelativeToleranceException() {
        new BrentOptimizer(0.00001, 0.0001);
    }

    // Test for edge case where absolute threshold is not strictly positive
    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbsoluteThresholdException() {
        new BrentOptimizer(0.0001, 0.0);
    }

}