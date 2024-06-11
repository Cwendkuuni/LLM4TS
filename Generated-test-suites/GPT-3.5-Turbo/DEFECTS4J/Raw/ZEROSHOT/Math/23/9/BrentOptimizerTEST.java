import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        });
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsParameter() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelParameter() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-16, 1e-6);
    }

    @Test
    public void testDoOptimize() {

        // Test case 1
        BrentOptimizer optimizer1 = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();

        // Test case 2
        BrentOptimizer optimizer2 = new BrentOptimizer(1e-9, 1e-9);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();

        // Assertions
        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
    }

    @Test
    public void testBest() {
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1.0, 2.0);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(1.5, 1.0);
        boolean isMinim = true;

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.best(point1, point2, isMinim);

        Assert.assertNotNull(result);
        Assert.assertEquals(point2, result);
    }
}